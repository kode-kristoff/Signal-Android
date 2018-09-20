package org.thoughtcrime.securesms.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import org.thoughtcrime.securesms.util.TextSecurePreferences;

public abstract class ComposeKeyPressedListener implements View.OnKeyListener, View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

  private final Context          context;
  private final InputAwareLayout inputContainer;
  private final SendButton       sendButton;

  private ComposeText composeText;
  private int         beforeLength;

  public ComposeKeyPressedListener(@NonNull InputAwareLayout inputContainer, @NonNull SendButton sendButton) {
    this.inputContainer = inputContainer;
    this.context        = sendButton.getContext();
    this.sendButton     = sendButton;
  }

  public void bind(@NonNull ComposeText composeText) {
    this.composeText = composeText;

    composeText.setOnKeyListener(this);
    composeText.addTextChangedListener(this);
    composeText.setOnClickListener(this);
    composeText.setOnFocusChangeListener(this);
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      if (keyCode == KeyEvent.KEYCODE_ENTER) {
        if (TextSecurePreferences.isEnterSendsEnabled(context)) {
          sendButton.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
          sendButton.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onClick(View v) {
    inputContainer.showSoftkey(composeText);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count,int after) {
    beforeLength = composeText.getTextTrimmed().length();
  }

  public abstract void textChanged(int lengthBefore);

  @Override
  public void afterTextChanged(Editable s) {
    textChanged(beforeLength);
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before,int count) {}

  @Override
  public void onFocusChange(View v, boolean hasFocus) {}
}
