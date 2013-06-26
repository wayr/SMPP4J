package org.wayr.smpp.alphabet;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayr.smpp.GlobalConfiguration;

/**
 *
 * @author paul
 */
public abstract class AlphabetEncodingFactory {

    protected static final Logger logger = LoggerFactory.getLogger(AlphabetEncodingFactory.class);
    private final Map<Integer, AlphabetEncoding> mappingTable = new HashMap<Integer, AlphabetEncoding>();
    private final Map<String, AlphabetEncoding> langToAlphabet = new HashMap<String, AlphabetEncoding>();
    private AlphabetEncoding defaultAlphabet;

    public AlphabetEncodingFactory(AlphabetEncoding defaultAlphabetEncoding, String lang) {
        this.defaultAlphabet = defaultAlphabetEncoding;
        this.mappingTable.put(new Integer(defaultAlphabetEncoding.getDataCoding()), defaultAlphabetEncoding);
        this.langToAlphabet.put(lang, defaultAlphabetEncoding);
    }

    public void addEncoding(final AlphabetEncoding encoding) {
        mappingTable.put(new Integer(encoding.getDataCoding()), encoding);
    }

    public void addEncoding(final AlphabetEncoding encoding, String lang) {
        this.addEncoding(encoding);
        this.setAlphabet(lang, encoding);
    }

    public void addEncoding(final Class<AlphabetEncoding> encodingClass) {
        try {
            Constructor c = encodingClass.getConstructor(new Class[0]);
            this.addEncoding((AlphabetEncoding) c.newInstance(new Object[0]));

        } catch (NoSuchMethodException x) {
            GlobalConfiguration.getInstance().getLogger().error(encodingClass.getName() + " does not have a no-argument constructor", x);
        } catch (IllegalAccessException x) {
            GlobalConfiguration.getInstance().getLogger().error(encodingClass.getName() + " does not have a visible no-argument constructor", x);
        } catch (InstantiationException x) {
            GlobalConfiguration.getInstance().getLogger().error("Cannot instantiate an instance of " + encodingClass, x);
        } catch (InvocationTargetException x) {
            Throwable cause = x.getCause();
            if (cause instanceof UnsupportedEncodingException) {
                GlobalConfiguration.getInstance().getLogger().debug(encodingClass.getName() + " is not supported by the JVM");
            } else {
                GlobalConfiguration.getInstance().getLogger().error(encodingClass.getName() + " constructor threw an exception", x);
            }
        } catch (IllegalArgumentException x) {
            GlobalConfiguration.getInstance().getLogger().error("This exception shouldn't happen", x);
        }
    }

    public AlphabetEncoding getEncoding(final int dataCoding) {
        return mappingTable.get(dataCoding);
    }

    public AlphabetEncoding getDefaultAlphabet() {
        return defaultAlphabet;
    }

    public void setDefaultAlphabet(AlphabetEncoding encoding) {
        this.defaultAlphabet = encoding;
    }

    public AlphabetEncoding getAlphabet(String lang) {
        AlphabetEncoding enc = (AlphabetEncoding) langToAlphabet.get(lang);
        if (enc != null) {
            return enc;
        } else {
            return this.defaultAlphabet;
        }
    }

    public AlphabetEncodingFactory setAlphabet(String lang, AlphabetEncoding encoding){
        this.langToAlphabet.put(lang, encoding);
        return this;
    }
}
