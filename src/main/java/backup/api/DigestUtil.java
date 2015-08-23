package backup.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class DigestUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DigestUtil.class);

    public static final String REGEX_FILE = "file";
    public static final String REGEX_DIGEST = "digest";
    public static final String REGEX_SIZE = "size";

    public enum DigestAlg
    {
        MD5 (32),
        SHA1 (40),
        SHA256 (64);

        private final int len;

        DigestAlg(final int len)
        {
            this.len = len;
        }

        public int getLen()
        {
            return len;
        }

        public String getName()
        {
            return name();
        }
    }

    public interface DigestProgressListener
    {
        void update(String message);

        void update(String message,
                    long currFileLen,
                    long processedBytes,
                    long totalSize);
    }

    private final DigestAlg digestAlg;

    private final Pattern pattern;

    public DigestUtil(final DigestAlg digestAlg)
    {
        this.digestAlg = digestAlg;

        this.pattern = Pattern.compile(getRegex(digestAlg));
    }

    public static DigestUtil createMatching(final String fileInfoStr)
    {
        DigestUtil ret = null;

        for (final DigestAlg digestAlg : DigestAlg.values())
        {
            final Pattern pattern = Pattern.compile(getRegex(digestAlg));

            final Matcher matcher = pattern.matcher(fileInfoStr);

            if (matcher.matches())
            {
                ret = new DigestUtil(digestAlg);
                break;
            }
        }

        if (ret == null)
        {
            throw new IllegalArgumentException("no matching digest alg found for: " + fileInfoStr);
        }

        return ret;
    }

    private static String getRegex(final DigestAlg digestAlg)
    {
        final String digestName = digestAlg.getName();

        final String fileInfoRegex = "^" + digestName + " \\((?<" + REGEX_FILE + ">.*)\\) = (?<" + REGEX_DIGEST + ">[a-fA-F\\d]{" + digestAlg.getLen() + "}) (?<" + REGEX_SIZE + ">\\d.*)$";

        return fileInfoRegex;
    }

    public String toFileInfoStr(final FileInfo fileInfo)
    {
        final StringBuilder buf = new StringBuilder();

        buf.append(digestAlg.getName());
        buf.append(" (");
        buf.append(fileInfo.getFilename());
        buf.append(") = ");
        buf.append(fileInfo.getHash());
        buf.append(" ");
        buf.append(fileInfo.getSize());

        return buf.toString();
    }

    public FileInfoPath toFileInfo(final String fileInfoStr)
    {
        final Matcher matcher = pattern.matcher(fileInfoStr);

        if (!matcher.matches())
        {
            throw new IllegalArgumentException();
        }

        final String path = matcher.group(REGEX_FILE);
        final String digest = matcher.group(REGEX_DIGEST);
        final String sizeStr = matcher.group(REGEX_SIZE);

        final File file = new File(path);
        final String fileBaseName = file.getName();

        final long size = Long.parseLong(sizeStr);

        final FileInfo fileInfo = new FileInfo(fileBaseName,
                                               digest,
                                               size);

        return new FileInfoPath(fileInfo,
                                path);
    }

    private HashFunction getHashFunction()
    {
        final HashFunction hashFunction;

        switch (digestAlg)
        {
            case MD5:
            {
                hashFunction = Hashing.md5();
                break;
            }
            case SHA1:
            {
                hashFunction = Hashing.sha1();
                break;
            }
            case SHA256:
            {
                hashFunction = Hashing.sha256();
                break;
            }
            default:
            {
                throw new IllegalArgumentException("unknown digest: " + digestAlg);
            }
        }

        return hashFunction;
    }

    private String getDigest(final File file) throws IOException
    {
        final HashFunction hashFunction = getHashFunction();

        final HashCode hashCode = com.google.common.io.Files.hash(file, hashFunction);

        return hashCode.toString();
    }

    public FileInfo toFileInfo(final File file) throws IOException
    {
        final String digest = getDigest(file);

        final long size = file.length();

        final FileInfo fileInfo = new FileInfo(file.getCanonicalPath(),
                                               digest,
                                               size);

        return fileInfo;
    }

    public void print(final FileManager fileManager)
    {
        final Map<FileInfo, List<String>> map = fileManager.getMap();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            LOGGER.info("### " + toFileInfoStr(fileInfo));

            for (final String path : paths)
            {
                LOGGER.info("#      " + path);
            }
        }
    }

    private void validateDir(final File dir) throws IOException
    {
        if (!dir.isDirectory())
        {
            throw new IOException("invalid directory: " + dir);
        }
    }

    private void validateDirs(final File dir1,
                              final File dir2) throws IOException
    {
        if (Files.isSameFile(dir1.toPath(), dir2.toPath()))
        {
            throw new IOException("same dirs: " + dir1);
        }

        if (dir1.toPath().startsWith(dir2.toPath()))
        {
            throw new IOException("invalid directory: " + dir1);
        }

        if (dir2.toPath().startsWith(dir1.toPath()))
        {
            throw new IOException("invalid directory: " + dir1);
        }
    }

    public File createDigestFile(final File digestDir,
                                 final File outputDir,
                                 final DigestProgressListener progress) throws IOException
    {
        validateDir(digestDir);
        validateDir(outputDir);

        validateDirs(digestDir, outputDir);

        final StopWatch stopWatch = new StopWatch().start();

        final Path digestPath = Paths.get(digestDir.toURI());

        final String outputFilename = "out_" + BackupUtil.getFilenameTimestamp() + "_" + digestAlg.getName().toLowerCase() + "_" + digestDir.getName();

        final File outputFile = new File(outputDir, outputFilename + ".txt");
        final File outputFileErr = new File(outputDir, outputFilename + ".err.txt");

        LOGGER.info("Writing digest file: " + outputFile);

        progress.update("Calculating directory size");

        final AtomicLong fileCount = new AtomicLong(0);

        final long totalDirectorySize = calcDirectorySize(digestPath, fileCount);

        progress.update("Starting directory digest",
                        -1,
                        0,
                        totalDirectorySize);

        final AtomicLong processedSize = new AtomicLong(0);
        final AtomicLong processedCount = new AtomicLong(0);
        final AtomicLong errorCount = new AtomicLong(0);

        try (final BufferedWriter writer = BackupUtil.createWriter(outputFile);
             final BufferedWriter writerErr = BackupUtil.createWriter(outputFileErr))
        {
            writeInfo(writer,
                      writerErr,
                      stopWatch,
                      outputFile,
                      digestDir,
                      totalDirectorySize,
                      fileCount,
                      processedCount,
                      errorCount);

            final FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(final Path filePath,
                                                 final BasicFileAttributes attrs) throws IOException
                {
                    try
                    {
                        final File file = filePath.toFile();

                        final String sizeStr = BackupUtil.humanReadableByteCount(file.length());
                        final String fileStr = file.getAbsolutePath() + " - " + sizeStr;

                        progress.update("> " + fileStr,
                                        file.length(),
                                        processedSize.longValue(),
                                        totalDirectorySize);

                        final FileInfo fileInfo = toFileInfo(file);

                        final String fileInfoStr = toFileInfoStr(fileInfo);

                        //LOGGER.info(fileInfoStr);

                        writeLine(writer, fileInfoStr, false);

                        progress.update("< " + fileStr,
                                        file.length(),
                                        processedSize.addAndGet(file.length()),
                                        totalDirectorySize);

                        processedCount.incrementAndGet();
                    }
                    catch (final IOException e)
                    {
                        visitFileFailed(filePath, e);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file,
                                                       final IOException exc) throws IOException
                {
                    writeLine(writerErr, file + " - err: " + exc.getMessage(), false);

                    errorCount.incrementAndGet();

                    return FileVisitResult.CONTINUE;
                }
            };

            Files.walkFileTree(digestPath, fileVisitor);

            stopWatch.stop();

            writeInfo(writer,
                      writerErr,
                      stopWatch,
                      outputFile,
                      digestDir,
                      totalDirectorySize,
                      fileCount,
                      processedCount,
                      errorCount);
        }

        return outputFile;
    }

    private void writeInfo(final Writer writer,
                           final Writer writerErr,
                           final StopWatch stopWatch,
                           final File outputFile,
                           final File digestDir,
                           final long totalDirectorySize,
                           final AtomicLong fileCount,
                           final AtomicLong processedCount,
                           final AtomicLong errorCount) throws IOException
    {
        writeInfo(writer,
                  stopWatch,
                  outputFile,
                  digestDir,
                  totalDirectorySize,
                  fileCount,
                  processedCount,
                  errorCount);

        writeInfo(writerErr,
                  stopWatch,
                  outputFile,
                  digestDir,
                  totalDirectorySize,
                  fileCount,
                  processedCount,
                  errorCount);
    }

    private void writeInfo(final Writer writer,
                           final StopWatch stopWatch,
                           final File outputFile,
                           final File digestDir,
                           final long totalDirectorySize,
                           final AtomicLong fileCount,
                           final AtomicLong processedCount,
                           final AtomicLong errorCount) throws IOException
    {
        writeLine(writer, "Started: " + stopWatch.getStartDate(), true);

        if (stopWatch.isStopped())
        {
            writeLine(writer, "Finished: " + stopWatch.getEndDate(), true);
        }

        writeLine(writer, "Duration: " + stopWatch.getDuration(), true);

        writeLine(writer, "Output path: " + outputFile.getAbsolutePath(), true);
        writeLine(writer, "Output file: " + outputFile.getName(), true);
        writeLine(writer, "Digest dir: " + digestDir.getAbsolutePath(), true);
        writeLine(writer, "File count: " + fileCount, true);
        writeLine(writer, "Processed count: " + processedCount, true);
        writeLine(writer, "Error count: " + errorCount, true);
        writeLine(writer, "Directory size: " + BackupUtil.humanReadableByteCount(totalDirectorySize), true);
    }

    private void writeLine(final Writer writer,
                           final String line,
                           final boolean isComment) throws IOException
    {
        if (isComment)
        {
            writer.write("# ");
        }

        writer.write(line);
        writer.write("\n");
    }

    private long calcDirectorySize(final Path digestPath,
                                   final AtomicLong fileCount) throws IOException
    {
        final AtomicLong totalSize = new AtomicLong(0);

        final FileVisitor<Path> sizeCalculator = new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult visitFile(final Path file,
                                             final BasicFileAttributes attrs) throws IOException
            {
                totalSize.addAndGet(file.toFile().length());

                fileCount.incrementAndGet();

                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree(digestPath, sizeCalculator);

        return totalSize.longValue();
    }
}
