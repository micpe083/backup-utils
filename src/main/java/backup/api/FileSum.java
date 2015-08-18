package backup.api;


public class FileSum
{
    public static final class CountSize
    {
        private long count;
        private long size;

        public void incr(final long countAdd,
                         final long sizeAdd)
        {
            count += countAdd;
            size += sizeAdd;
        }

        public long getCount()
        {
            return count;
        }

        public String getCountStr()
        {
            return BackupUtil.formatNumber(count);
        }

        public long getSize()
        {
            return size;
        }

        public String getSizeStr()
        {
            return BackupUtil.humanReadableByteCount(size);
        }

        @Override
        public String toString()
        {
            return getCountStr() + "/" + getSizeStr();
        }
    }

    private final CountSize total = new CountSize();
    private final CountSize unique = new CountSize();
    private final CountSize duplicate = new CountSize();

    public CountSize getTotal()
    {
        return total;
    }

    public CountSize getUnique()
    {
        return unique;
    }

    public CountSize getDuplicate()
    {
        return duplicate;
    }

    @Override
    public String toString()
    {
        final String str = "Tot: " + getTotal() +
                           " - Unique: " + getUnique() +
                           " - Dups: " + getDuplicate();

        return str;
    }
}
