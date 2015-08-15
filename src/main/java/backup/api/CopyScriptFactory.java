package backup.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import backup.gui.common.BackupSettings;

public final class CopyScriptFactory
{
    private CopyScriptFactory() {}

    public static void createCopyScript(final FileManager fileManager) throws IOException
    {
        createCopyScript(fileManager,
                         BackupSettings.getInstance().getString(BackupSettings.COPY_SCRIPT_OUTPUT_DIR),
                         BackupSettings.getInstance().getString(BackupSettings.COPY_SCRIPT_BASE_DIR_FROM),
                         BackupSettings.getInstance().getString(BackupSettings.COPY_SCRIPT_BASE_DIR_TO));
    }

    private static void createCopyScript(final FileManager fileManager,
                                         final String outputDir,
                                         final String baseDirFrom,
                                         final String baseDirTo) throws IOException
    {
        final FileManager fileManagerUnique = fileManager.getUniquePaths();

        final File file = new File(outputDir, "copy-unique-" + System.currentTimeMillis() + ".sh");

        try (final BufferedWriter writer = BackupUtil.createWriter(file))
        {
            writer.write("#!/bin/sh\n");
            writer.write('\n');

            writer.write("startTime=$(date +%s)\n");
            writer.write('\n');

            writer.write("copy_with_parents()\n");
            writer.write("{\n");
            writer.write("    mkdir -p \"${2}\"\n");
            writer.write("    cp -p \"${1}\" \"${2}\"\n");
            writer.write("}\n");

            writer.write('\n');
            writer.write('\n');

            createCopyScript(writer,
                             fileManagerUnique,
                             baseDirFrom,
                             baseDirTo);

            writer.write('\n');

            writer.write("endTime=$(date +%s)\n");
            writer.write('\n');
            writer.write("elapsed=$(( $endTime - $startTime ))\n");
            writer.write('\n');
            writer.write("echo elapsed seconds: ${elapsed}\n");
            writer.write('\n');
        }
    }

    private static void createCopyScript(final BufferedWriter writer,
                                         final FileManager fileManager,
                                         final String baseDirFrom,
                                         final String baseDirTo) throws IOException
    {
        for (final Entry<FileInfo, List<String>> entry : fileManager.getMap().entrySet())
        {
            final String fromPath = entry.getValue().get(0);

            final String toPathTmp = baseDirTo + fromPath.substring(baseDirFrom.length(), fromPath.length());
            final String toPath = toPathTmp.substring(0, toPathTmp.lastIndexOf('/') + 1);

            final String out = "copy_with_parents \"" + fromPath + "\" \"" + toPath + "\"";

            writer.write(out);
            writer.write('\n');
            writer.flush();
        }
    }
}
