package backup.api;

public abstract class FileSum
{
    private final String type;

    private final long total;
    private final long unique;
    private final long duplicate;

    protected FileSum(final String type,
                      final long total,
                      final long unique,
                      final long duplicate)
    {
        this.type = type;

        this.total = total;
        this.unique = unique;
        this.duplicate = duplicate;
    }

    public String getType()
    {
        return type;
    }

    public long getTotal()
    {
        return total;
    }

    public String getTotalStr()
    {
        return format(total);
    }

    public long getUnique()
    {
        return unique;
    }

    public String getUniqueStr()
    {
        return format(unique);
    }

    public long getDuplicate()
    {
        return duplicate;
    }

    public String getDuplicateStr()
    {
        return format(duplicate);
    }

    @Override
    public String toString()
    {
        final String str = getType() +
                           " - Tot: " + getTotalStr() +
                           " - Unique: " + getUniqueStr() +
                           " - Dups: " + getDuplicateStr();

        return str;
    }

    protected abstract String format(long val);

    public static FileSum createSize(final long total,
                                     final long unique,
                                     final long duplicate)
    {
        final FileSum fileSum = new FileSum("Size",
                                            total,
                                            unique,
                                            duplicate)
        {
            @Override
            protected String format(final long val)
            {
                return BackupUtil.humanReadableByteCount(val);
            }
        };

        return fileSum;
    }

    public static FileSum createCount(final long total,
                                      final long unique,
                                      final long duplicate)
    {
        final FileSum fileSum = new FileSum("Count",
                                            total,
                                            unique,
                                            duplicate)
        {
            @Override
            protected String format(final long val)
            {
                return BackupUtil.formatNumber(val);
            }
        };

        return fileSum;
    }
}
