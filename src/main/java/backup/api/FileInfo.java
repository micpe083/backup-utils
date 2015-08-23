package backup.api;

import backup.api.DigestUtil.DigestAlg;


public final class FileInfo
{
    private final String filename;
    private final String digest;
    private final long size;

    public FileInfo(final String filename,
                    final String hash,
                    final long size)
    {
        this.filename = filename;
        this.digest = hash;
        this.size = size;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getHash()
    {
        return digest;
    }

    public long getSize()
    {
        return size;
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
        return true;
    }
}
