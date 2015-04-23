#include "fileExists.h"

// Sprawdza, czy plik istnieje.
bool fileExists(const std::string& fname) {
	std::ifstream s(fname);
	return s.good();
}
