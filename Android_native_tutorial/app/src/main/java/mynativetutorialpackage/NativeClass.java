package mynativetutorialpackage;

public class NativeClass {
    //The function takes a pointer to the image 'addrRgba'
    public native static void testFunction(long addrRgba);
}
