
/*Pass*/


struct myStruct {
   int x;
   int y;
   int[2][2] z;
}


fun int main() {
   var int x;
   var int y;
   var myStruct ms = {{1},{2},{{1,2},{1,2}}};
   var char* c = func(x, y, ms);
}

fun int func(char* x, int y, myStruct ms) {
   return 0;
}
