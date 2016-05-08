package backup.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import backup.api.DigestUtil.DigestAlg;

public class TestDigestUtil
{
    @Test
    public void testToFileInfoStrMD5()
    {
        testToFileInfoStr(DigestAlg.MD5,
                          new FileInfo("/a/a.txt", "9e107d9d372bb6826bd81d3542a419d6", 111, 1462730880411L),
                          "MD5 (/a/a.txt) = 9e107d9d372bb6826bd81d3542a419d6 111 2016-05-08T18:08:00.411Z");
    }

    @Test
    public void testToFileInfoStrSHA1()
    {
        testToFileInfoStr(DigestAlg.SHA1,
                          new FileInfo("/a/a.txt", "de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3", 111, 1462730880411L),
                          "SHA1 (/a/a.txt) = de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3 111 2016-05-08T18:08:00.411Z");
    }

    @Test
    public void testToFileInfoStrSHA256()
    {
        testToFileInfoStr(DigestAlg.SHA256,
                          new FileInfo("/a/a.txt", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 111, 1462730880411L),
                          "SHA256 (/a/a.txt) = e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 111 2016-05-08T18:08:00.411Z");
    }

    private void testToFileInfoStr(final DigestAlg digestAlg,
                                   final FileInfo fileInfoExcpected,
                                   final String fileInfoStrExcpected)
    {
        final DigestUtil digestUtil = new DigestUtil(digestAlg);

        final String fileInfoStrActual = digestUtil.toFileInfoStr(fileInfoExcpected);

        assertEquals(fileInfoStrExcpected, fileInfoStrActual);
    }

    @Test
    public void testToFileInfoMD5()
    {
        testToFileInfo(DigestAlg.MD5,
                       new FileInfo("a.txt", "9e107d9d372bb6826bd81d3542a419d6", 111, 1462730880411L),
                       "MD5 (/a/a.txt) = 9e107d9d372bb6826bd81d3542a419d6 111 2016-05-08T18:08:00.411Z",
                       "/a/a.txt");
    }

    @Test
    public void testToFileInfoSHA1()
    {
        testToFileInfo(DigestAlg.SHA1,
                       new FileInfo("a.txt", "de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3", 111, 1462730880411L),
                       "SHA1 (/a/a.txt) = de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3 111 2016-05-08T18:08:00.411Z",
                       "/a/a.txt");
    }

    @Test
    public void testToFileInfoSHA256()
    {
        testToFileInfo(DigestAlg.SHA256,
                       new FileInfo("a.txt", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 111, 1462730880411L),
                       "SHA256 (/a/a.txt) = e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 111 2016-05-08T18:08:00.411Z",
                       "/a/a.txt");
    }

    private void testToFileInfo(final DigestAlg digestAlg,
                                final FileInfo fileInfoExcpected,
                                final String fileInfoStrExcpected,
                                final String pathExcpected)
    {
        final DigestUtil digestUtil = new DigestUtil(digestAlg);

        final FileInfoPath fileInfoPathActual = digestUtil.toFileInfo(fileInfoStrExcpected);

        assertEquals(fileInfoExcpected, fileInfoPathActual.getFileInfo());

        assertEquals(pathExcpected, fileInfoPathActual.getPath());

        final FileInfo fileInfo = new FileInfo(pathExcpected,
                                               fileInfoExcpected.getDigest(),
                                               fileInfoExcpected.getSize(),
                                               fileInfoExcpected.getLastModified());

        assertEquals(fileInfoStrExcpected, digestUtil.toFileInfoStr(fileInfo));
    }

    @Test
    public void testDateFormat() throws Exception
    {
        testDateFormat(1111, "1970-01-01T00:00:01.111Z");
        testDateFormat(0, "1970-01-01T00:00:00Z");
        testDateFormat(1462730880411L, "2016-05-08T18:08:00.411Z");
    }

    private void testDateFormat(final long lastModifiedLong,
                                final String lastModifiedStr) throws Exception
    {
        final String actualLastModifiedStr = DigestUtil.getFileLastModified(lastModifiedLong);
        assertEquals(lastModifiedStr, actualLastModifiedStr);

        final long actualLastModifiedLong = DigestUtil.getFileLastModified(lastModifiedStr);
        assertEquals(lastModifiedLong, actualLastModifiedLong);
    }
}
