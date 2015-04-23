#include "EnemyFactory.h"

Enemy* EnemyFactory::getRandomEnemy(int xpos, int ypos) {
	if (Random::tryChance(90)) return new NormalEnemy(xpos, ypos);
	else return new HardEnemy(xpos, ypos);
}
