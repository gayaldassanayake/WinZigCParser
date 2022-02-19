package compiler.util;

public class Validator {
    public static boolean validateArgs(String[] args) throws IllegalArgumentException {
        if(args.length==1 && (args[0].equals("--help") || args[0].equals("-h"))) {
            help();
            return false;
        } else if(args.length==2 && args[0].equals("-ast")) {
            return true;
        } else {
            throw new IllegalArgumentException("Only one or two arguments allowed");
        }
    }

    public static void help() {
        System.out.println("WinZigC lexical analyzer and compiler.parser:");
        System.out.println("    -h [--help]     show help message");
        System.out.println("    -ast arg        produce AST for the program");
        System.out.println("Example execution 1:");
        System.out.println("    java winzigc –ast winzig_test_programs /winzig_01");
        System.out.println("Example execution 2:");
        System.out.println("    java winzigc –ast winzig_test_programs /winzig_02 > tree.02");
    }
}
