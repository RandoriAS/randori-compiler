package randori.compiler.bundle.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

import randori.compiler.bundle.IBundle;
import randori.compiler.bundle.IBundleCategory;
import randori.compiler.bundle.IBundleContainer;
import randori.compiler.bundle.IBundleEntry;
import randori.compiler.bundle.IBundleFileEntry;
import randori.compiler.bundle.IBundleLibrary;

public class BundleDirectoryWriter extends BundleWriterBase
{

    private File directory;

    public BundleDirectoryWriter(String path)
    {
        this.directory = new File(path);
    }

    @Override
    void prepare(IBundle bundle) throws IOException
    {
        if (!directory.exists())
        {
            if (!directory.mkdir())
                throw new FileNotFoundException(directory.getAbsolutePath());
        }
    }

    @Override
    void writeManifest(IBundle bundle) throws IOException
    {
        final Writer writer = new BufferedWriter(new FileWriter(new File(
                directory, MANIFEST_XML)));
        writeManifestXML(bundle, writer);
        writer.flush();
        writer.close();
    }

    @Override
    void writeLibrary(IBundleLibrary library) throws IOException
    {
        for (IBundleContainer container : library.getContainers())
        {
            for (IBundleCategory entry : container.getCategories())
            {
                for (IBundleEntry element : entry.getEntries())
                {
                    writeFile(element);
                }
            }
        }
    }

    //@Override
    void writeFile(IBundleEntry entry) throws IOException
    {
        final File file = new File(directory, entry.getPath())
                .getAbsoluteFile();
        final File parentFolder = file.getParentFile();
        if (!parentFolder.isDirectory())
            parentFolder.mkdirs();
        file.createNewFile();
        final OutputStream outputStream = new FileOutputStream(file);
        final InputStream fileInputStream = entry.createInputStream();
        IOUtils.copy(fileInputStream, outputStream);
        fileInputStream.close();
        outputStream.close();
    }

    @Override
    void writeFile(IBundleFileEntry entry) throws IOException
    {
        final File file = new File(directory, entry.getPath())
                .getAbsoluteFile();
        final File parentFolder = file.getParentFile();
        if (!parentFolder.isDirectory())
            parentFolder.mkdirs();
        file.createNewFile();
        final OutputStream outputStream = new FileOutputStream(file);
        final InputStream fileInputStream = entry.createInputStream();
        IOUtils.copy(fileInputStream, outputStream);
        fileInputStream.close();
        outputStream.close();
    }

    @Override
    void finish(IBundle bundle) throws IOException
    {
    }

}
