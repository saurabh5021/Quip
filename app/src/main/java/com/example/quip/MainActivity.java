package com.example.quip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quip.Fragments.ChatsFragment;
import com.example.quip.Fragments.ProfileFragment;
import com.example.quip.Fragments.UsersFragment;
import com.example.quip.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private String[] titles = new String[]{"Chats", "Users", "Profile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "" );


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference( "Users" ).child( firebaseUser.getUid());

        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue( User.class );
                username.setText( user.getUsername() );
                if (user.getImageURL() !=null && user.getImageURL().equals( "default" )) {
                    profile_image.setImageResource( R.mipmap.ic_launcher );
                } else {

                    Glide.with( MainActivity.this ).load( user.getImageURL() ).into( profile_image );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final TabLayout tabLayout = findViewById( R.id.tab_layout );
        final ViewPager2 viewPager = findViewById( R.id.view_pager );

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,((tab, position) -> tab.setText(titles[position]))).attach();
        


//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragment( new ChatsFragment(), "Chats" );
//        viewPagerAdapter.addFragment( new UsersFragment(), "Users" );
//        viewPagerAdapter.addFragment( new UsersFragment(), "Profile" );
//
//        viewPager.setAdapter( viewPagerAdapter );
//
//        new TabLayoutMediator(tabLayout, viewPager,(((tab, position) -> tab.setText(getTitle())))).attach();

        //tabLayout.setupWithViewPager(viewPager)



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent( MainActivity.this, LoginActivity.class ).setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish();
//
//                FirebaseAuth.getInstance()
//                        .signOut(this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                // user is now signed out
//                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                                finish();
//                            }
//                        });
                return true;
        }

        return false;
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference( "Users" ).child( firebaseUser.getUid() );

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put( "status", status );

        reference.updateChildren( hashMap );
    }

    @Override
    protected void onResume() {
        super.onResume();
        status( "online" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        status( "offline" );
    }

      class ViewPagerAdapter extends FragmentStateAdapter{



          public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
              super(fragmentActivity);
//            this.fragments = new ArrayList<>();
//            this.titles = new ArrayList<>();
          }

          @NonNull
          @Override
          public Fragment createFragment(int position) {
              switch (position){
                  case 0:
                      return new ChatsFragment();
                  case 1:
                      return new UsersFragment();
                  case 2:
                      return new ProfileFragment();
              }
              return new ChatsFragment();
          }

          @Override
          public int getItemCount() {
              return titles.length;
          }

//          public void addFragment(Fragment fragment, String title) {
//            fragments.add( fragment );
//            titles.add( title );
//        }
//
//          @Nullable
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }

//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
      }
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private ArrayList<Fragment> fragments;
//        private ArrayList<String> titles;
//
//
//        public ViewPagerAdapter(@NonNull FragmentManager fm) {
//            super(fm);
//            this.fragments = new ArrayList<>();
//            this.titles = new ArrayList<>();
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return fragments.get( position );
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            fragments.add( fragment );
//            titles.add( title );
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
//    }




}