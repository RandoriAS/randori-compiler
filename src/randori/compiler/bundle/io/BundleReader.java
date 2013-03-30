package randori.compiler.bundle.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.flex.compiler.problems.FileInLibraryIOProblem;
import org.apache.flex.compiler.problems.FileInLibraryNotFoundProblem;
import org.apache.flex.compiler.problems.LibraryNotFoundProblem;

import randori.compiler.bundle.Bundle;
import randori.compiler.bundle.IBundle;

public class BundleReader implements IBundleReader
{

    private static final String MANIFEST_XML = "manifest.xml";

    private File bundleFile;

    private Bundle bundle;

    private StAXManifestReader catalogReader;

    public BundleReader(String filename)
    {
        this.bundleFile = new File(filename);
        this.bundle = new Bundle(bundleFile);

        if (!bundleFile.exists() || !bundleFile.isFile())
        {
            bundle.addProblem(new LibraryNotFoundProblem(bundleFile
                    .getAbsolutePath()));
            return;
        }

        ZipFile zipFile = null;
        catalogReader = null;
        try
        {
            try
            {
                zipFile = new ZipFile(bundleFile, ZipFile.OPEN_READ);
                final InputStream catalogInputStream = getInputStream(zipFile,
                        MANIFEST_XML);
                if (catalogInputStream == null)
                {
                    bundle.addProblem(new FileInLibraryNotFoundProblem(
                            bundleFile.getAbsolutePath(), MANIFEST_XML));
                    return;
                }
                catalogReader = new StAXManifestReader(new BufferedInputStream(
                        catalogInputStream), bundle);
                catalogReader.parse();
                catalogReader.close();
                catalogReader = null;
            }
            catch (Exception e)
            {
                bundle.addProblem(new FileInLibraryIOProblem(MANIFEST_XML,
                        bundleFile.getAbsolutePath(), e.getLocalizedMessage()));
                return;
            }
        }
        finally
        {
            try
            {
                if (catalogReader != null)
                    catalogReader.close();

                if (zipFile != null)
                    zipFile.close();
            }
            catch (Exception e)
            {
                // ignore
            }
        }
    }

    @Override
    public File getFile()
    {
        return bundleFile;
    }

    @Override
    public IBundle getBundle()
    {
        return bundle;
    }

    public static InputStream getInputStream(ZipFile zipFile, String filename)
            throws IOException
    {
        ZipEntry zipEntry = null;
        for (final Enumeration<? extends ZipEntry> entryEnum = zipFile
                .entries(); entryEnum.hasMoreElements();)
        {
            final ZipEntry entry = entryEnum.nextElement();
            if (entry.getName().equals(filename))
            {
                zipEntry = entry;
                break;
            }
        }

        if (zipEntry == null)
            return null;
        else
            return zipFile.getInputStream(zipEntry);
    }
}
