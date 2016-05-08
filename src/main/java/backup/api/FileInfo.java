package backup.api;

import backup.api.DigestUtil.DigestAlg;


public final class FileInfo
{
    public static final boolean USE_FILE_TIMESTAMP = true;

    private final String filename;
    private final String digest;
    private final long size;
    private final long lastModified;

    public FileInfo(final String filename,
                    final String digest,
                    final long size,
                    final long lastModified)
    {
        this.filename = filename;
        this.digest = digest;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getDigest()
    {
        return digest;
    }

    public long getSize()
    {
        return size;
    }

    public long getLastModified()
    {
        return lastModified;
    }

    public String getLastModifiedStr()
    {
        return DigestUtil.getFileLastModified(lastModified);
    }

    public DigestAlg getDigestAlg()
    {
        DigestAlg ret = null;

        for (final DigestAlg digestAlg : DigestAlg.values())
        {
            if (digest.length() == digestAlg.getLen())
            {
                ret = digestAlg;
                break;
            }
        }

        return ret;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + ((digest == null) ? 0 : digest.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));

        if (USE_FILE_TIMESTAMP)
        {
            result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
        }

        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        final FileInfo other = (FileInfo) obj;

        if (filename == null)
        {
            if (other.filename != null)
            {
                return false;
            }
        }
        else if (!filename.equals(other.filename))
        {
            return false;
        }

        if (digest == null)
        {
            if (other.digest != null)
            {
                return false;
            }
        }
        else if (!digest.equals(other.digest))
        {
            return false;
        }

        if (size != other.size)
        {
            return false;
        }

        if (USE_FILE_TIMESTAMP)
        {
            if (lastModified != other.lastModified)
            {
                return false;
            }
        }

        return true;
    }
}
