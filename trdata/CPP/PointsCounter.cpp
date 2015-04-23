#include "PointsCounter.h"

PointsCounter::PointsCounter(std::string fname) :
		TextObject(0, 0, 20, sf::Color::Red), pointsCount(0), fname(fname) {
	readHighScore();
}

void PointsCounter::readHighScore() {
	if (fileExists(fname)) {
		std::ifstream f(fname);
		f >> highScore;
		f.close();
	} else
		highScore = 0;
	updateString();
}

void PointsCounter::writeHighScore() {
	if (pointsCount > highScore) {
		std::ofstream f(fname);
		if (!f.is_open()) return; // pewnie brak uprawnień - nie męczymy usera
		f << pointsCount;
		f.close();
	}
}
