#include <iostream>
#include "Game.h"
#include "FileNotFoundException.h"
int main() {

	std::cout << "Startujemy gre" << std::endl;

	try {
		Game().mainloop();
	} catch (FileNotFoundException& e) {
		std::cerr << "Nie mozna znalezc pliku " << e.getFileName() << std::endl;
	} catch (...) {
		std::cout << "Wystapil nieznany blad" << std::endl;
	}

}
