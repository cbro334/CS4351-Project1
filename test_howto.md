# Testing

To run syntax and semantic test scripts, 
run commands in terminal in this format:

sh TEST_SCRIPT TEST_FILE_REFERENCE

Examples include:

sh test_syntax.sh Tests/test1.c
sh test_semantic.sh Tests/test1.c

sh test_syntax.sh Tests/test2.c
sh test_semantic.sh Tests/test2.c

... and so on ...

There are currently 16 test files within ./Tests directory
