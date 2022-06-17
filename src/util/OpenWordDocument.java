/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author takacs.gergely
 */
public class OpenWordDocument {

    public static void open(String path, int pageNum) {
        openFile(path);
        if (pageNum>1) {
            waitforFile();
            goToPage(pageNum);
        }
    }
    
    public static void open(String path) {
        openFile(path);        
    }
    
    private static void pressButton(Robot robot, int buttonCode) {
        robot.keyPress(buttonCode);
        robot.keyRelease(buttonCode);
    }

    private static void enterPageNumber(Robot robot, int pageNum) {
        String numText = String.valueOf(pageNum);
        for (int i = 0; i < numText.length(); i++) {
            char numChar = numText.charAt(i);
            int number = Integer.parseInt(String.valueOf(numChar));
            int numberKeyCodeStart = 48;
            pressButton(robot, numberKeyCodeStart + number);
        }
    }

    private static void getPage(Robot robot, int pageNum) {
        pressMouseButton(robot);
        pressButton(robot, KeyEvent.VK_F5);
        enterPageNumber(robot, pageNum);
        pressButton(robot, KeyEvent.VK_ENTER);
        pressButton(robot, KeyEvent.VK_TAB);
        pressButton(robot, KeyEvent.VK_TAB);
        pressButton(robot, KeyEvent.VK_ENTER);
    }
    
    private static void waitforFile(){
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            Logger.getLogger(OpenWordDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void openFile(String path) {
        try {
            File file = new File(path);
            if (!Desktop.isDesktopSupported())
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) 
            {
                desktop.edit(file);                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void goToPage(int pageNum) {
        try {
            getPage(new Robot(), pageNum);
        } catch (AWTException ex) {
            Logger.getLogger(OpenWordDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void pressMouseButton(Robot robot) {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    

}
