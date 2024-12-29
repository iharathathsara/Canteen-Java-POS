/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Random;

/**
 *
 * @author Ihara
 */
public class UserUtils {

    // Method to generate a unique ID
    public static String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(999); // Three-digit random number
        return "U_" + timestamp + String.format("%03d", randomNumber);
    }
}

