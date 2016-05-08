package backup.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class DigestFileUtil
{
    private static final int BUFFER = 2048;

    private DigestFileUtil()
    {
    }

    public static File create(final File outputZipFile,
                              final File outputFile,
                              final File outputFileErr) throws Exception
    {
        DigestFileUtil.zip(outputZipFile,
                           outputFile,
                           outputFileErr);

        Files.delete(outputFile.toPath());
        Files.delete(outputFileErr.toPath());

        return outputFile;
    }

    public static File getDigestFile(final File zipFile) throws Exception
    {
        return unzip(zipFile,
                     DigestUtil.DIGEST_FILE_TYPE);
    }

    public static File getDigestErrFile(final File zipFile) throws Exception
    {
        return unzip(zipFile,
                     DigestUtil.DIGEST_FILE_TYPE_ERR);
    }

    private static void zip(final File zipFile,
                            final File... files) throws IOException
    {
        try (final FileOutputStream fos = new FileOutputStream(zipFile);
             final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos)))
        {
            final byte[] data = new byte[BUFFER];

            for (final File file : files)
            {
                try (final FileInputStream fis = new FileInputStream(file);
                     final BufferedInputStream origin = new BufferedInputStream(fis, BUFFER))
                {
                    final ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    int readCount;
                    while ((readCount = origin.read(data, 0, BUFFER)) != -1)
                    {
                        zos.write(data, 0, readCount);
                    }
                }
            }
        }
    }

    private static File unzip(final File zipFile,
                              final String suffix) throws Exception
    {
        File ret = null;

        try (final FileInputStream fis = new FileInputStream(zipFile);
             final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis)))
        {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null)
            {
                if (entry.getName().endsWith(suffix))
                {
                    ret = Files.createTempFile(entry.getName() + "-", suffix).toFile();
                    ret.deleteOnExit();

                    try (final FileOutputStream fos = new FileOutputStream(ret);
                         final BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER))
                    {
                        int count;
                        final byte[] data = new byte[BUFFER];

                        while ((count = zis.read(data, 0, BUFFER)) != -1)
                        {
                            dest.write(data, 0, count);
                        }

                        dest.flush();
                    }

                    break;
                }
            }
        }

        return ret;
    }
}
