package com.ordonteam.hanabi.activity

import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.HanabiGame
import groovy.transform.CompileStatic

@CompileStatic
class AchievementsAndLeaderboards {
    private GameActivity gameActivity

    AchievementsAndLeaderboards(GameActivity gameActivity) {
        this.gameActivity = gameActivity
    }

    void update(HanabiGame hanabi) {
        int score = hanabi.score()

        gameActivity.unlock(R.string.achievement_first_match);
        gameActivity.increaseScore(R.string.leaderboard_total_points, score)
        if (score == 25) {
            gameActivity.unlock(R.string.achievement_first_perfect_firework);
            gameActivity.increment(R.string.achievement_3_prefect_fireworks);
            gameActivity.increment(R.string.achievement_5_perfect_fireworks);
            gameActivity.increaseScore(R.string.leaderboard_perfect_fireworks, 1)
        }
        if (hanabi.thundersNumber == 0) {
            gameActivity.unlock(R.string.achievement_wrong_move);
        }

        int players = hanabi.players.size()
        if (players == 2) {
            gameActivity.unlock(R.string.achievement_two_people_game)
        } else if (players == 3) {
            gameActivity.unlock(R.string.achievement_three_people_game)
        } else if (players == 4) {
            gameActivity.unlock(R.string.achievement_four_people_game)
        } else if (players == 5) {
            gameActivity.unlock(R.string.achievement_five_people_game)
        }
    }
}
