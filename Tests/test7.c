
/*Fail*/
union myStruct {
   int x;
   int y;
   int[2][2] z;
}


fun int main() {
   var myStruct ms = {{1},{2},{{1,2},{1,2}}};
}
