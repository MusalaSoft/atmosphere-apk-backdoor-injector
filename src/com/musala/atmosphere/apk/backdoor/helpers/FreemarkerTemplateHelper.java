package com.musala.atmosphere.apk.backdoor.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.musala.atmosphere.apk.backdoor.exceptions.FreemarkerInflateException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * A class that can be used to interact with Freemarker template engine.
 * 
 * @author boris.strandjev
 */
public class FreemarkerTemplateHelper {

    /**
     * Inflates the given template using the given parmeters.
     * 
     * @param templateLocation
     *        - the location of the freemarker template to inflate
     * @param inflateLocation
     *        - the location at which to create the file based on the template. It should include the name of the file
     *        to create itself. If any of the parent directories of this file does not exist, it will be created in the
     *        process
     * @param inflateParameters
     *        - the parameters to use while inflating the template
     */
    public void inflateTemplate(File templateLocation, File inflateLocation, Map<String, String> inflateParameters) {
        File parentFile = inflateLocation.getAbsoluteFile().getParentFile();
        parentFile.mkdirs();
        try (Writer file = new FileWriter(inflateLocation)) {
            @SuppressWarnings("deprecation")
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(templateLocation.getAbsoluteFile().getParentFile());
            Template template = configuration.getTemplate(templateLocation.getName());
            template.process(inflateParameters, file);
            file.flush();
        } catch (IOException e) {
            new FreemarkerInflateException("Problem accessing / writing freemarker templates.", e);
        } catch (TemplateException e) {
            new FreemarkerInflateException("Problem in freemarker templates.", e);
        }
    }
}
