#!/bin/bash

java -cp bin client.src.Main write 1 aaa
java -cp bin client.src.Main write 2 bbb
sleep 1
java -cp bin client.src.Main write 3 ccc
java -cp bin client.src.Main write 4 ddd
sleep 1
java -cp bin client.src.Main write 5 eee
java -cp bin client.src.Main write 6 fff
sleep 1
java -cp bin client.src.Main write 7 ggg
java -cp bin client.src.Main write 8 hhh
sleep 1
java -cp bin client.src.Main write 9 iii
java -cp bin client.src.Main write 10 jjj

