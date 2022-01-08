package utils.user.upload;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("User/UploadProfileImage")
    Call<ResponseBody> Upload(@Part MultipartBody.Part file,@Part MultipartBody.Part userId);
}
