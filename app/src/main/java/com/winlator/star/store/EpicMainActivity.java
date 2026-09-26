package com.winlator.star.store;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Entry point for the Epic Games integration.
 *
 * Shows login card or signed-in card based on EpicCredentialStore login state.
 * Launched from the side menu (ID=12 / 0xc).
 */
public class EpicMainActivity extends Activity {

    private LinearLayout loginCard;
    private LinearLayout loggedInCard;
    private TextView     statusView;

    // Epic brand color
    private static final int COLOR_EPIC = 0xFF0078F0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF0D0D0D);

        loginCard    = buildLoginCard();
        loggedInCard = buildLoggedInCard();

        root.addView(loginCard,    new FrameLayout.LayoutParams(-1, -1));
        root.addView(loggedInCard, new FrameLayout.LayoutParams(-1, -1));

        setContentView(root);
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    private void refreshView() {
        if (loginCard == null) return;
        boolean loggedIn = EpicCredentialStore.isLoggedIn(this);
        loginCard.setVisibility(loggedIn ? View.GONE  : View.VISIBLE);
        loggedInCard.setVisibility(loggedIn ? View.VISIBLE : View.GONE);

        if (loggedIn && statusView != null) {
            EpicCredentialStore.Credentials creds = EpicCredentialStore.load(this);
            if (creds != null) {
                String name = (creds.displayName != null && !creds.displayName.isEmpty())
                        ? creds.displayName : "Epic Account";
                long minutesLeft = (creds.expiresAt - System.currentTimeMillis()) / 60000L;
                statusView.setText("已登录为 " + name + "\nToken expires in ~" + minutesLeft + " min");
            }
        }
    }

    private LinearLayout buildLoginCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFF0F1117);
        card.setGravity(Gravity.CENTER);
        int pad = dp(40);
        card.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("Epic Games");
        title.setTextSize(32f);
        title.setTextColor(COLOR_EPIC);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView sub = new TextView(this);
        sub.setText("登录以访问你的 Epic 游戏库");
        sub.setTextSize(14f);
        sub.setTextColor(0xFFAAAAAA);
        sub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-2, -2);
        subLp.topMargin = dp(16);
        card.addView(sub, subLp);

        Button loginBtn = new Button(this);
        loginBtn.setText("使用 Epic Games 登录");
        loginBtn.setBackgroundColor(COLOR_EPIC);
        loginBtn.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(-2, dp(48));
        btnLp.topMargin = dp(24);
        loginBtn.setOnClickListener(v ->
                startActivity(new Intent(this, EpicLoginActivity.class)));
        card.addView(loginBtn, btnLp);

        return card;
    }

    private LinearLayout buildLoggedInCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFF0F1117);
        card.setGravity(Gravity.CENTER);
        int pad = dp(40);
        card.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("Epic Games");
        title.setTextSize(32f);
        title.setTextColor(COLOR_EPIC);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        statusView = new TextView(this);
        statusView.setText("");
        statusView.setTextSize(13f);
        statusView.setTextColor(0xFFCCCCCC);
        statusView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams statusLp = new LinearLayout.LayoutParams(-2, -2);
        statusLp.topMargin = dp(16);
        card.addView(statusView, statusLp);

        Button libraryBtn = new Button(this);
        libraryBtn.setText("查看游戏库");
        libraryBtn.setBackgroundColor(COLOR_EPIC);
        libraryBtn.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams libLp = new LinearLayout.LayoutParams(-2, dp(48));
        libLp.topMargin = dp(24);
        libraryBtn.setOnClickListener(v ->
                startActivity(new Intent(this, EpicGamesActivity.class)));
        card.addView(libraryBtn, libLp);

        Button signOutBtn = new Button(this);
        signOutBtn.setText("退出登录");
        signOutBtn.setBackgroundColor(0xFF444444);
        signOutBtn.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams soLp = new LinearLayout.LayoutParams(-2, dp(48));
        soLp.topMargin = dp(16);
        signOutBtn.setOnClickListener(v -> signOut());
        card.addView(signOutBtn, soLp);

        return card;
    }

    private void signOut() {
        EpicCredentialStore.clear(this);
        refreshView();
        Toast.makeText(this, "已退出 Epic Games", Toast.LENGTH_SHORT).show();
    }
}
