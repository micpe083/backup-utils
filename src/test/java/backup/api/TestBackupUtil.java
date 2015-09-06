package backup.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestBackupUtil
{
    @Test
    public void testFindCommonPathWin()
    {
        checkValues("c:\\test1\\", "c:\\test1\\test21\\testA.txt", "c:\\test1\\test22\\testB.txt");
        checkValues("c:\\", "c:\\testA.txt", "c:\\test1\\test22\\testB.txt");
        checkValues("c:\\", "c:\\test11\\testA.txt", "c:\\test12\\testB.txt");
    }

    @Test
    public void testFindCommonPathUnix()
    {
        checkValues("/test1/", "/test1/test21/testA.txt", "/test1/test22/testB.txt");
        checkValues("/", "/testA.txt", "/test12/testB.txt");
        checkValues("/", "/test11/testA.txt", "/test12/testB.txt");
    }

    private void checkValues(final String expected,
                             final String path1,
                             final String path2)
    {
        String actual = BackupUtil.findCommonPath(path1, path2);
        assertEquals(expected, actual);

        actual = BackupUtil.findCommonPath(path2, path1);
        assertEquals(expected, actual);
    }
}
