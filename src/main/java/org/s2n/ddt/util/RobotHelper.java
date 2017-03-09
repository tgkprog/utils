package org.s2n.ddt.util;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.s2n.ddt.bean.UtlConf;

public class RobotHelper {
    private static Logger logger = LogManager.getLogger(RobotHelper.class);
    private static java.awt.Robot robot;

    static {
        try {
            logger.log(Level.DEBUG, "awt Robot init ");
            robot = new java.awt.Robot();
            logger.log(Level.INFO, "awt Robot created ");
        } catch (AWTException e) {
            logger.log(Level.ERROR, "awt Robot not created, non window system ? " + e, e);

        }
    }

    public static boolean sendKeys(String s, int delay, boolean shiftKey, boolean altKey, boolean enterKey, boolean otherKeyTrigger,
            char otherKey, boolean metaKey) {
        try {
            Thread.sleep(100);

            if (shiftKey) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }

            if (metaKey) {
                robot.keyPress(KeyEvent.VK_META);
            }

            if (otherKeyTrigger) {
                robot.keyPress(otherKey);
            }

            if (altKey) {
                robot.keyPress(KeyEvent.VK_ALT);
            }
            sendKeys(s, delay);

            if (enterKey) {
                robot.keyPress(KeyEvent.VK_ENTER);
                Thread.sleep(delay);
                robot.keyPress(KeyEvent.VK_ENTER);
            }

            if (shiftKey) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }

            if (otherKeyTrigger) {
                robot.keyRelease(otherKey);
            }

            if (metaKey) {
                robot.keyRelease(KeyEvent.VK_META);
            }

            if (enterKey) {
                robot.keyRelease(KeyEvent.VK_ENTER);
            }

            if (altKey) {
                robot.keyRelease(KeyEvent.VK_ALT);
            }
            sendEnterKey();

        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public static boolean sendKeys(String s, int delay) throws InterruptedException {
        if (s == null) {
            return true;
        }
        try {
            Thread.sleep(100);
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                char c = Character.toUpperCase(ch);
                switch (c) {
                case '\n':
                    robot.keyPress(KeyEvent.VK_ENTER);
                    Thread.sleep(delay);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;

                case '\t':
                    robot.keyPress(KeyEvent.VK_TAB);
                    Thread.sleep(delay);
                    robot.keyRelease(KeyEvent.VK_TAB);
                default:
                    robot.keyPress(c);
                    Thread.sleep(delay);
                    robot.keyRelease(c);
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int sendEnterKey() {

        try {

            robot.keyPress(KeyEvent.VK_ENTER);
            // LangUtils.sleep(delay);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(5000);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Robot sendKeys :" + e, e);
        }
        return 0;
    }

    public static boolean tabOut() {
        try {
            robot.keyPress(KeyEvent.VK_TAB);
            String sd = UtlConf.getProperty("robot.tabOutAction.sleep", "250");
            int delay = Integer.parseInt(sd);// some sleep between keys is a good idea
            LangUtils.sleep(delay);
            robot.keyRelease(KeyEvent.VK_TAB);
            return true;
        } catch (Exception e) {
            logger.log(Level.ERROR, "Robot sendKeys :" + e, e);
            return false;
        }

    }

}
