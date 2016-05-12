package org.teiid.test.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.spec.DHParameterSpec;


public class Reproduce {
    
    private static String ALGORITHM = "DiffieHellman"; 
    
    private static DHParameterSpec DH_SPEC;
    private static DHParameterSpec DH_SPEC_2048;
    
    static {
        DH_SPEC = loadKeySpecification("dh.properties"); //$NON-NLS-1$
        DH_SPEC_2048 = loadKeySpecification("dh-2048.properties"); //$NON-NLS-1$
    }
    
    private static DHParameterSpec loadKeySpecification(String propsFile) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = Reproduce.class.getClassLoader().getResourceAsStream(propsFile); 
//            is = new FileInputStream(new File("/home/ren/work/dh-2048.properties"));
            props.load(is); 
        } catch (IOException e) {
              throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        BigInteger p = new BigInteger(props.getProperty("p")); //$NON-NLS-1$
        BigInteger g = new BigInteger(props.getProperty("g")); //$NON-NLS-1$
        DHParameterSpec result = new DHParameterSpec(p, g, Integer.parseInt(props.getProperty("l"))); //$NON-NLS-1$
        return result;
    }
    

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//        keyGen.initialize(DH_SPEC_2048);
//        keyGen.initialize(DH_SPEC);
        
        DHParameterSpec params = (DHParameterSpec)DH_SPEC_2048;
        int pSize = params.getP().bitLength();
        
        System.out.println(pSize);
    }

}
