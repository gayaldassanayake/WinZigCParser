public class Validator {
    public static String validateArgs(String[] args) throws IllegalArgumentException {
        if(args.length!=1) {
            throw new IllegalArgumentException("Only one argument allowed");
        }
        return args[0];
    }

    public static int help(String arg) {
        if(arg.equals("--help") || arg.equals("-h")) {
            // TODO: complete the help command
            System.out.println("Help command");
            return -1;
        }
        return 0;
    }
}
