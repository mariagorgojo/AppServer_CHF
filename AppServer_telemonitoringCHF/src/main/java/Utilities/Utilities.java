/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.util.Random;

/**
 *
 * @author mariagorgojo
 */
public class Utilities {
    public static int generateRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound) + 1;
    }
    
}
