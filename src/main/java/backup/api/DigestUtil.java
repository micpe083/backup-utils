package backup.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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

    private final DigestAlg digestAlg;

    private final Pattern pattern;

    public DigestUtil(final DigestAlg digestAlg)
    {
        this.digestAlg = digestAlg;

        this.pattern = Pattern.compile(getRegex(digestAlg));
    }

    private String getRegex(final DigestAlg digestAlg)
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
                                 final File outputDir) throws IOException
    {
        validateDir(digestDir);
        validateDir(outputDir);

        validateDirs(digestDir, outputDir);

        final Path digestPath = Paths.get(digestDir.toURI());

        final String outputFilename = "out_" + digestAlg.getName().toLowerCase() + "_" + digestDir.getName() + "_" + BackupUtil.getFilenameTimestamp() + ".txt";

        final File outputFile = new File(outputDir, outputFilename);

        LOGGER.info("Writing digest file: " + outputFile);

        try (final BufferedWriter writer = BackupUtil.createWriter(outputFile))
        {
            final FileVisitor<Path> fileVisitor = new FileVisitor<Path>()
            {
                @Override
                public FileVisitResult postVisitDirectory(final Path dir,
                                                          final IOException exc) throws IOException
                {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(final Path dir,
                                                         final BasicFileAttributes attrs) throws IOException
                {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file,
                                                 final BasicFileAttributes attrs) throws IOException
                {
                    final FileInfo fileInfo = toFileInfo(file.toFile());

                    final String fileInfoStr = toFileInfoStr(fileInfo);

                    LOGGER.info(fileInfoStr);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file,
                                                       final IOException exc) throws IOException
                {
                    LOGGER.error("failed to process file: " + file, exc);

                    return FileVisitResult.CONTINUE;
                }
            };

            Files.walkFileTree(digestPath, fileVisitor);
        }

        return outputFile;
    }
}
