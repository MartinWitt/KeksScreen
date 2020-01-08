package xyz.keksdose.keksscreen;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Start {

    public static void main(String[] args) {
        // see here for reason https://bugs.openjdk.java.net/browse/JDK-8214233
        System.setProperty("sun.java2d.uiScale", "1.0");
        disableLogger();
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new KeyBoardListener());
        System.out.println("Start abgeschlossen");
    }

    private static void disableLogger() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }
}
