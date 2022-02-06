package com.cs4542.compiler.util;

public class Validator {
    public static String validateArgs(String[] args) throws IllegalArgumentException {
        if(args.length!=1) {
            throw new IllegalArgumentException("Only one argument allowed");
        }
        return args[0];
    }

    public static int help(String arg) {
        if(arg.equals("--help") || arg.equals("-h")) {
            System.out.println("WinZigC lexical analyzer and parser:");
            System.out.println("    -h [--help] show help message");
            System.out.println("Example execution:");
            System.out.println("    java winzigc –ast winzig_test_programs /winzig_02 > tree.02");
            return -1;
        }
        return 0;
    }
}
