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
                          new FileInfo("/a/a.txt", "9e107d9d372bb6826bd81d3542a419d6", 111),
                          "MD5 (/a/a.txt) = 9e107d9d372bb6826bd81d3542a419d6 111");
    }

    @Test
    public void testToFileInfoStrSHA1()
    {
        testToFileInfoStr(DigestAlg.SHA1,
                          new FileInfo("/a/a.txt", "de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3", 111),
                          "SHA1 (/a/a.txt) = de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3 111");
    }

    @Test
    public void testToFileInfoStrSHA256()
    {
        testToFileInfoStr(DigestAlg.SHA256,
                          new FileInfo("/a/a.txt", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 111),
                          "SHA256 (/a/a.txt) = e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 111");
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
                       new FileInfo("a.txt", "9e107d9d372bb6826bd81d3542a419d6", 111),
                       "MD5 (/a/a.txt) = 9e107d9d372bb6826bd81d3542a419d6 111",
                       "/a/a.txt");
    }

    @Test
    public void testToFileInfoSHA1()
    {
        testToFileInfo(DigestAlg.SHA1,
                       new FileInfo("a.txt", "de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3", 111),
                       "SHA1 (/a/a.txt) = de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3 111",
                       "/a/a.txt");
    }

    @Test
    public void testToFileInfoSHA256()
    {
        testToFileInfo(DigestAlg.SHA256,
                       new FileInfo("a.txt", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 111),
                       "SHA256 (/a/a.txt) = e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 111",
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
    }
}

