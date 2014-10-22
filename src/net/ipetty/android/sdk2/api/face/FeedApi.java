/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk2.api.face;

import java.util.Date;
import java.util.List;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.FeedWithLocationVO;
import net.ipetty.vo.ImageVO;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 *
 * @author Administrator
 */
public interface FeedApi {

        @Multipart
        @POST(value = "/feed/publishImage")
        public ImageVO publishImage(@Part(value = "imageFile") TypedFile tf);

        @FormUrlEncoded
        @POST(value = "/feed/publishText")
        public FeedVO publishText(@Field(value = "text") String string, @Field(value = "imageId") Long imageId);

        @FormUrlEncoded
        @POST(value = "/feed/publishText2")
        public FeedVO publishTextWithLocation(@Body FeedWithLocationVO feedWithLocationVO);

        @GET(value = "/feed/{id}")
        public FeedVO getById(@Path(value = "id") Long id);

        @GET(value = "/feed/square")
        public List<FeedVO> listByTimelineForSquare(@Query(value = "username") Date timeline, @Query(value = "pageNumber") int pageNumber, @Query(value = "pageSize") int pageSize);

        @GET(value = "/feed/home")
        public List<FeedVO> listByTimelineForHomePage(@Query(value = "username") Date timeline, @Query(value = "pageNumber") int pageNumber, @Query(value = "pageSize") int pageSize);

        @GET(value = "/feed/space")
        public List<FeedVO> listByTimelineForSpace(@Query(value = "userId") Integer userId, @Query(value = "username") Date timeline, @Query(value = "pageNumber") int pageNumber, @Query(value = "pageSize") int pageSize);

        @GET(value = "/feed/comments/{feedId}")
        public List<CommentVO> listCommentsByFeedId(@Part(value = "feedId") Long feedId);

        @GET(value = "/feed/favors/{feedId}")
        public List<FeedFavorVO> listFavorsByFeedId(@Part(value = "feedId") Long feedId);

        @POST(value = "/feed/comment")
        public FeedVO comment(@Body CommentVO cvo);

        /**
         * 删除消息
         */
        @POST(value = "/feed/delete")
        public boolean delete(@Query(value = "id") Long id);

        /**
         * 删除评论
         */
        @POST(value = "/feed/comment/delete")
        public boolean deleteComment(@Query(value = "id") Long id);

        @POST(value = "/feed/favor")
        public FeedVO favor(@Body FeedFavorVO ffvo);

        @POST(value = "/feed/unfavor")
        public FeedVO unfavor(@Body FeedFavorVO ffvo);

}
