package com.ltu.fm.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 * @author PhuLTU
 */
public final class RandomUtil {

    /** The Constant DEF_COUNT. */
    private static final int DEF_COUNT = 20;

    /**
     * Instantiates a new random util.
     */
    private RandomUtil() {
    	
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
    
    /**
     * Generate activation key.
     *
     * @param numCount the num count
     * @return the string
     */
    public static String generateActivationKey(int numCount) {
        return RandomStringUtils.randomNumeric(numCount);
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
   public static String generateResetKey() {
       return RandomStringUtils.randomNumeric(DEF_COUNT);
   }
   
}
