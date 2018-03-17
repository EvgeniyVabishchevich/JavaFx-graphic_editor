package editor;

public class EditorLog {

    public static boolean debugMode = true;

    public static void log(String s)
    {
        if (debugMode)
        {
            System.out.println(s);
        }
    }
}
