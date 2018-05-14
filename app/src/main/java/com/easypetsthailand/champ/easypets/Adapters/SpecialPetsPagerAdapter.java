package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.easypetsthailand.champ.easypets.Fragments.SpecialPetsFragment;

public class SpecialPetsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SpecialPetsPagerAdapter.class.getSimpleName();
    private Context context;

    public SpecialPetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SpecialPetsFragment.newInstance("reptiles");
            case 1:
                return SpecialPetsFragment.newInstance("birds");
            case 2:
                return SpecialPetsFragment.newInstance("aquatics");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //fragments quantity
        return 3;
    }

}
