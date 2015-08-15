package backup.api;

public final class FileInfo
{
    private final String filename;
    private final String hash;
    private final long size;

    public FileInfo(final String filename,
                    final String hash,
                    final long size)
    {
        this.filename = filename;
        this.hash = hash;
        this.size = size;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getHash()
    {
        return hash;
    }

    public long getSize()
    {
        return size;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileInfo other = (FileInfo) obj;
        if (filename == null)
        {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        if (hash == null)
        {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;
        if (size != other.size)
            return false;
        return true;
    }
}
