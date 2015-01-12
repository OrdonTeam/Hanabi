package com.ordonteam.hanabi.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.google.android.gms.games.Games
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.gms.AbstractGamesActivity
import com.ordonteam.hanabi.inject.InjectConstants
import groovy.transform.CompileStatic

@CompileStatic
class LeaderboardsDialog extends AlertDialog.Builder {
    LeaderboardsDialog(AbstractGamesActivity activity) {
        super(activity)
        setTitle(R.string.dialog_leadreboard);
        setMessage(R.string.dialog_leadreboard_text);
        setPositiveButton(R.string.dialog_leadreboard_total, { DialogInterface dialog, int whichButton ->
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(activity.client,
                    activity.getString(R.string.leaderboard_total_points)),
                    InjectConstants.REQUEST_LEADERBOARD_TOTAL);
        });
        setNegativeButton(R.string.dialog_leadreboard_perfect, { DialogInterface dialog, int whichButton ->
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(activity.client,
                    activity.getString(R.string.leaderboard_perfect_fireworks)),
                    InjectConstants.REQUEST_LEADERBOARD_PERFECT);
        });
    }
}
