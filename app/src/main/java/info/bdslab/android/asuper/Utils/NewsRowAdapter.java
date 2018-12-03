package info.bdslab.android.asuper.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import info.bdslab.android.asuper.POJO.Article;
import info.bdslab.android.asuper.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by nikola on 13/10/2017.
 */

public class NewsRowAdapter extends ArrayAdapter<Article> {

    public final String TAG = "NewsRowAdapter log";

        private Activity activity;
        private List<Article> articles;
        private int row;
        private DisplayImageOptions options;
        ImageLoader imageLoader;

        public NewsRowAdapter(Activity act, int resource, List<Article> arrayList) {
            super(act, resource, arrayList);
            this.activity = act;
            this.row = resource;
            this.articles = arrayList;

            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.ic_placeholder)
                    .showImageForEmptyUri(R.mipmap.ic_placeholder).cacheInMemory()
                    .cacheOnDisc().build();

                imageLoader = ImageLoader.getInstance();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

//            Log.e(TAG, "inside getView method:" + position);

            Article objBean;
            View view = convertView;
            ViewHolder holder;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(row, null);

                holder = new ViewHolder();
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if ((articles == null) || ((position + 1) > articles.size())) {
                return view;
            }

            objBean = articles.get(position);

            holder.tvTitle = (TextView) view.findViewById(R.id.tvtitle);
//            holder.tvDescription = (TextView) view.findViewById(R.id.tvdescription);
            holder.tvPubDate = (TextView) view.findViewById(R.id.tvpubdate);
//            holder.tvSource = (TextView) view.findViewById(R.id.tvsource);
            holder.imgView = (ImageView) view.findViewById(R.id.image);
            holder.slogoView = (ImageView) view.findViewById(R.id.tvSmallSourceLogo);
            holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

            if (holder.tvTitle != null && null != objBean.getTitle()
                    && objBean.getTitle().trim().length() > 0) {
                holder.tvTitle.setText(Html.fromHtml(objBean.getTitle()));
            }
//            if (holder.tvDescription != null && null != objBean.getDescription()
//                    && objBean.getDescription().trim().length() > 0) {
//                holder.tvDescription.setText(Html.fromHtml(objBean.getDescription()));
//            }
            if (holder.tvPubDate != null && null != objBean.getPubDate()
                    && objBean.getPubDate().trim().length() > 0) {
                Date d = new Date(Long.parseLong(objBean.getPubDate())*1000L);
                holder.tvPubDate.setText(Html.fromHtml(d.toString()));
            }
//            if (holder.tvSource != null && null != objBean.getSource()
//                    && objBean.getSource().trim().length() > 0) {
//                holder.tvSource.setText(Html.fromHtml(objBean.getSource()));
//            }
            // Article image view
            if (holder.imgView != null) {
                if (null != objBean.getImage()
                        && objBean.getImage().trim().length() > 0) {
                    final ProgressBar pbar = holder.pbar;
                    if(!imageLoader.isInited()){
                        imageLoader.init(ImageLoaderConfiguration
                                .createDefault(activity));
                    }
                    if(objBean.getImage().startsWith("/")){
                        objBean.setImage("http://balkans.aljazeera.net"+objBean.getImage());
                    }

                    if(!objBean.getImage().endsWith("jpg") && !objBean.getImage().endsWith("png") && objBean.getImage().endsWith("bmp") && objBean.getImage().contains("?")){

                        Log.d(TAG, "Image: "+ objBean.getImage().substring(0,objBean.getImage().indexOf("?")));
                        objBean.setImage(objBean.getImage().substring(0,objBean.getImage().indexOf("?")));

                    }
                    imageLoader.displayImage(objBean.getImage(), holder.imgView,
                            options, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    pbar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    pbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    pbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                    pbar.setVisibility(View.INVISIBLE);
                                }
                            });

                } else {
                    holder.imgView.setImageResource(R.drawable.ic_launcher);
                }
            }
            // Article source logo image view
            if (holder.slogoView != null) {
                if (null != objBean.getLogo()
                        && objBean.getLogo().trim().length() > 0) {

                    if(!imageLoader.isInited()){
                        imageLoader.init(ImageLoaderConfiguration
                                .createDefault(activity));
                    }
                    if(!objBean.getLogo().endsWith("jpg") && !objBean.getLogo().endsWith("png") && !objBean.getLogo().endsWith("bmp") && objBean.getImage().contains("?")){
                        Log.d(TAG, "Image: "+ objBean.getImage().substring(0,objBean.getImage().indexOf("?")));
                        objBean.setLogo(objBean.getLogo().substring(0,objBean.getLogo().indexOf("?")));
                    }
                    imageLoader.displayImage(objBean.getLogo(), holder.slogoView,
                            options, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
//                                    pbar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                                    pbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                    pbar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
//                                    pbar.setVisibility(View.INVISIBLE);
                                }
                            });

                } else {
                    holder.slogoView.setVisibility(View.INVISIBLE);
                }
            }

            return view;
        }

        public class ViewHolder {

            public TextView tvTitle, tvDescription, tvSource, tvPubDate;
            private ImageView imgView, slogoView;
            private ProgressBar pbar;
        }
}
