#include "Game.h"

Game::Game() : window(sf::VideoMode(CONF_screenWidth, CONF_screenHeight, 32), CONF_windowTitle),
	ship(0.4), pc("highscore.lst") {
	window.setFramerateLimit(CONF_frameRateLimit);

}

void Game::reset() {
	masterClock.restart();
	pc.reset(); // licznik punktów := 0

	for (unsigned int i = 0; i < missiles.size(); ++i) delete missiles[i]; missiles.clear();
	for (unsigned int i = 0; i < enemies.size(); ++i) delete enemies[i]; enemies.clear();
	for (unsigned int i = 0; i < bonuses.size(); ++i) delete bonuses[i]; bonuses.clear();
	window.clear();
}

void Game::addEnemy() {
	// pozycja enemy to lewy górny róg jego prostokąta!
	Enemy* e = EnemyFactory::getRandomEnemy(0, 0); // dowolne, trzeba tak bo niżej getSize()
	int xpos = Random::getInt(0, CONF_screenWidth - e->getSize()); // maksimum - prawa krawędź ekranu
	int ypos = Random::getInt(0, CONF_screenHeight)*CONF_enemyDownLimit; // górna część miejscem na enemy
	e->setPosition(xpos, ypos);
	enemies.push_back(e);
}

void Game::addBonus() {
	Bonus* b = new SimpleBonus(0, 0);
	int xpos = Random::getInt(0, CONF_screenWidth - b->getSize());
	int ypos = Random::getInt(0, CONF_screenHeight)*CONF_bonusDownLimit; // górna część miejscem na bonusy też
	b->setPosition(xpos, ypos);
	bonuses.push_back(b);
}

bool Game::isCollision(sf::Vector2f mpos, sf::Vector2f epos, int msize, int esize) { // pociski są kwadratami!!!
	// nie wiem jak, ale to działa xD

	sf::Vector2f msizes(msize, msize);
	sf::Vector2f esizes(esize, esize);

	sf::Rect<float> mrect(mpos, msizes);
	sf::Rect<float> erect(epos, esizes);

	return mrect.intersects(erect);
}

void Game::recalc() {
	// pociski poza ekranem
againMissilesOff:
	for (auto m = missiles.begin(); m != missiles.end(); ++m) {
		if ((*m)->getPosition().y <= 0) {
			delete *m; missiles.erase(m);
			goto againMissilesOff;
		}
	}

	// bonusy poza ekranem
againBonusesOff:
	for (auto b = bonuses.begin(); b != bonuses.end(); ++b) {
		if ((*b)->getPosition().y >= CONF_screenHeight) {
			delete *b; bonuses.erase(b);
			goto againBonusesOff;
		}
	}

againMissilesCrash: // sprawdzanie kolizji
	for (auto m = missiles.begin(); m != missiles.end(); ++m) {
		for (auto e = enemies.begin(); e != enemies.end(); ++e) { // elementy są wskaźnikami
			if (isCollision((*m)->getPosition(), (*e)->getPosition(), (*m)->getSize(), (*e)->getSize())) { // KOLIZJA JUPI

				int missileDamage = (*m)->getDamage();
				delete *m; missiles.erase(m);
				(*e)->damage(missileDamage);

				if (!((*e)->isAlive())) { // zestrzelony?
					pc.add((*e)->getPoints());
					delete *e;
					enemies.erase(e);
				}
				goto againMissilesCrash;
			}
		}
	}

// Pochłanianie bonusów
againBonusCatch:
	for (auto b = bonuses.begin(); b != bonuses.end(); ++b) { // elementy są wskaźnikami
		if (isCollision((*b)->getPosition(), ship.getPosition(), (*b)->getSize(), ship.getSize())) { // KOLIZJA JUPI
			pc.add((*b)->getPoints());
			delete *b; bonuses.erase(b);
			goto againBonusCatch;
		}
	}

	// Czy przypadkiem gracz nie wtopił
	for (auto e = enemies.begin(); e != enemies.end(); ++e) {
		if ((*e)->getPosition().y > (CONF_screenHeight*(1-CONF_shipUpLimit) - (*e)->getSize())) {
			throw GameOverException();
		}
	}
}

bool Game::loop() {
	sf::Event e;
	while (window.pollEvent(e)) { // nie zatnie się, w końcu jakieś nadejdzie
		if (e.type == sf::Event::Closed) {
			window.close();
			return false;
		}
	}
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Escape) || sf::Keyboard::isKeyPressed(sf::Keyboard::Q)) {
		window.close();
		return false;
	}

	// Restart głównego zegara odpowiadającego za szybkość animacyj
	sf::Time t = gameSpeedClock.restart();

	// Poruszanie statkiem
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)) {
		ship.move(Left, t);
	}
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)) {
		ship.move(Right, t);
	}
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)) {
		ship.move(Up, t);
	}
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)) {
		ship.move(Down, t);
	}

	if (sf::Keyboard::isKeyPressed(sf::Keyboard::R)) { // deweloperski trik
		reset();
		return true;
	}


	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Z)) { // NormalMissile
		if (NormalMissile::canBeSent()) {
			sf::Vector2f shipPos = ship.getPosition();
			missiles.push_back(new NormalMissile(shipPos.x, shipPos.y));
			NormalMissile::restartClock();
		}
	}
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::X)) { // PowerMissile
		if (PowerMissile::canBeSent()) {
			sf::Vector2f shipPos = ship.getPosition();
			missiles.push_back(new PowerMissile(shipPos.x, shipPos.y));
			PowerMissile::restartClock();
		}
	}

	if (Random::tryChance(CONF_enemyGenerationChance*(log(masterClock.getElapsedTime().asMilliseconds()/CONF_difficultyLevel)))) addEnemy();
	if (Random::tryChance(CONF_bonusGenerationChance)) addBonus();

	// Achtung!!! Ważne rzeczy się dzieją właśnie tu
	try {
		recalc();
	} catch (GameOverException&) {
		gameOver();
		reset();
	}

	// Rysowanie wszelkiego
	window.clear();

	// Linia
	line.draw(window);
	// Licznik punktów
	pc.draw(window);
	// Wskaźnik PowerMissile
	mind.update(); mind.draw(window);

	// Pociski
	for (auto& m : missiles) {
		m->moveIterate(t);
		m->draw(window);
	}

	// Przeciwnicy
	for (auto& e : enemies) {
		e->moveIterate(t);
		e->draw(window);
	}

	// Bonusy
	for (auto& b : bonuses) {
		b->moveIterate(t);
		b->draw(window);
	}

	// Statek
	ship.draw(window);

	window.display();

	return true; // kontynuacja gry
}

void Game::gameOver() {
	GameOverText gt;
	pc.writeHighScore();
	pc.readHighScore();
	gt.draw(window);
	window.display();

	// czekanie jednej sekundy
	sf::Clock cl;
	cl.restart();
	while (cl.getElapsedTime().asMilliseconds() < 1000);
}


Game::~Game() {
	reset(); // to czyści wektory
	window.close();
}
