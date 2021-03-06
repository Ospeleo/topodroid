/* @file AzimuthDialDialog.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief TopoDroid survey azimuth dialog 
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.DistoX;

import android.app.Dialog;
import android.os.Bundle;

import android.text.method.KeyListener;

import android.content.Context;
import android.content.DialogInterface;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import android.util.Log;


public class AzimuthDialDialog extends Dialog
                              implements View.OnClickListener
                              , IBearingAndClino
{
  private Context mContext;
  private ILister mParent;
  private float mAzimuth;
  private Bitmap mBMdial;

  private Button mBTback;
  private Button mBTfore;
  private Button mBTazimuth;
  private Button mBTsensor;
  private Button mBTok;
  private Button mBTleft;
  private Button mBTright;

  public AzimuthDialDialog( Context context, ILister parent, float azimuth, Bitmap dial )
  {
    super(context);
    mContext = context;
    mParent  = parent;
    mAzimuth = azimuth;
    mBMdial  = dial;
  }

  void updateView()
  {
    Matrix m = new Matrix();
    m.preRotate( mAzimuth - 90 );
    int w = 96; // mBMdial.getWidth();
    Bitmap bm1 = Bitmap.createScaledBitmap( mBMdial, w, w, true );
    Bitmap bm2 = Bitmap.createBitmap( bm1, 0, 0, w, w, m, true);
    mBTazimuth.setBackgroundDrawable( new BitmapDrawable( mContext.getResources(), bm2 ) );
  }

// -------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

    // TopoDroidLog.Log( TopoDroidLog.LOG_SHOT, "Shot Dialog::onCreate" );
    setContentView(R.layout.azimuth_dial_dialog);
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    mBTback = (Button) findViewById(R.id.btn_back );
    mBTfore = (Button) findViewById(R.id.btn_fore );
    mBTazimuth = (Button) findViewById(R.id.btn_azimuth );
    mBTsensor  = (Button) findViewById(R.id.btn_sensor );
    mBTok      = (Button) findViewById(R.id.btn_ok );
    mBTleft    = (Button) findViewById(R.id.btn_left );
    mBTright   = (Button) findViewById(R.id.btn_right );

    mBTback.setOnClickListener( this );
    mBTfore.setOnClickListener( this );
    mBTazimuth.setOnClickListener( this );
    mBTsensor.setOnClickListener( this );
    mBTok.setOnClickListener( this );
    mBTleft.setOnClickListener( this );
    mBTright.setOnClickListener( this );

    updateView();
  }

  public void setBearingAndClino( float b0, float c0 )
  {
    mAzimuth = b0;
    updateView();
  }

  TimerTask mTimer = null;

  public void onClick(View v) 
  {
    if ( mTimer != null ) mTimer.mRun = false;

    Button b = (Button) v;
    // TopoDroidLog.Log( TopoDroidLog.LOG_INPUT, "AzimuthDialDialog onClick button " + b.getText().toString() );

    if ( b == mBTback ) {
      mAzimuth -= 5;
      if ( mAzimuth < 0 ) mAzimuth += 360;
      updateView();
    } else if ( b == mBTfore ) {
      mAzimuth += 5;
      if ( mAzimuth >= 360 ) mAzimuth -= 360;
      updateView();
    } else if ( b == mBTazimuth ) {
      mAzimuth += 90;
      if ( mAzimuth >= 360 ) mAzimuth -= 360;
      updateView();
    } else if ( b == mBTsensor ) {
      mTimer = new TimerTask( mContext, this );
      mTimer.execute();
    } else if ( b == mBTok ) {
      mParent.setRefAzimuth( mAzimuth, 0 );
      dismiss();
    } else if ( b == mBTleft ) {
      mParent.setRefAzimuth( mAzimuth, -1L );
      dismiss();
    } else if ( b == mBTright ) {
      mParent.setRefAzimuth( mAzimuth, 1L );
      dismiss();
    } else {
      dismiss();
    }
  }

}

