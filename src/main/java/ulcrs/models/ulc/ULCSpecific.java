package ulcrs.models.ulc;

import com.google.gson.annotations.SerializedName;

public class ULCSpecific {

    @SerializedName("shiftID")
    private int shiftId;

    // Minimum number of tutors required per shift
    private int numTutors;


    ULCSpecific(int shiftId, int numTutors) {
        this.shiftId = shiftId;
        this.numTutors = numTutors;
    }

    public int getShiftId() {
        return shiftId;
    }

    public int getNumTutors() {
        return numTutors;
    }
}
