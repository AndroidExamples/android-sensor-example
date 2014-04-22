package be.hcpl.android.sensors;

import android.support.v4.app.Fragment;

/**
 * A base class for all fragments that will show up in the navigation bar. These all need a
 * properly implemented toString() implementation since we use a default listAdapter implementation
 */
public class BaseFragment extends Fragment {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * this isn't really needed but makes it easier to get the parent activity fragment for
     * switching content without having to cast and check for types and so.
     *
     * @return the parent activity as a MainActivity instance
     */
    protected MainActivity getParentActivity(){
        return (MainActivity)getActivity();
    }
}
