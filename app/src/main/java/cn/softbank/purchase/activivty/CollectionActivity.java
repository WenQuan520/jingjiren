package cn.softbank.purchase.activivty;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.softbank.purchase.adapter.BaseViewHolder;
import cn.softbank.purchase.adapter.CommonAdapter;
import cn.softbank.purchase.base.BaseActivity;
import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.HomeGoodsDatas;
import cn.softbank.purchase.network.BeanRequest;
import cn.softbank.purchase.network.JsonElementRequest;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;
import cn.softbank.purchase.network.AbstractRequest.MamaHaoError;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.CommonUtils;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ImageUtils;
import cn.yicheng.jingjiren.R;

public class CollectionActivity extends BaseActivity {

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.message_activity);
        initTitleBar("我的收藏", Constant.DEFAULT_LEFT_BACK, null);
    }

    private CommonAdapter<HomeGoodsDatas> adapter;
    private List<HomeGoodsDatas> datas = new ArrayList<>();

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        SwipeMenuListView lv = (SwipeMenuListView) findViewById(R.id.listview);
        adapter = new CommonAdapter<HomeGoodsDatas>(context, datas, R.layout.home_list_item) {

            @Override
            public void convert(BaseViewHolder holder, HomeGoodsDatas itemData,
                                int position, ViewGroup parent) {
                // TODO Auto-generated method stub
                holder.setImageByUrl(R.id.iv_img, itemData.getImage(), ImageUtils.imgOptionsBig);
                holder.setText(R.id.tv_title, itemData.getName());
                if (!TextUtils.isEmpty(itemData.getPrice()))
                    holder.setText(R.id.tv_price, itemData.getPrice() + "元/平米");
                else
                    holder.setText(R.id.tv_price, "待定");
                //类型
                TextView tv_type = holder.getView(R.id.tv_type);
                if (itemData.getFlag() != null && itemData.getFlag().size() > 0) {
                    tv_type.setVisibility(View.VISIBLE);
                    tv_type.setText(itemData.getFlag().get(0));
                } else
                    tv_type.setVisibility(View.GONE);
                //tv_area
                if (!TextUtils.isEmpty(itemData.getArea()))
                    holder.setText(R.id.tv_area, itemData.getArea());
                else
                    holder.setText(R.id.tv_area, "暂无信息");

                holder.setText(R.id.tv_distance, itemData.getDistance());
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                startActivity(new Intent(context, GoodsDetailActitvivty.class).putExtra("id", datas.get(position).getId()));
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(CommonUtils.dip2px(context, 90));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
//					deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lv.setMenuCreator(creator);

        // step 2. listener item click event
        lv.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                requestCancelCollect(datas.get(position).getId());
                return true;
            }

        });
//		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					final int position, long id) {
//				// TODO Auto-generated method stub
//				new YesOrNoDialog(context, new YesOrNoDialogEntity("确认取消收藏?", "否", "是"), new OnYesOrNoDialogClickListener() {
//					
//					@Override
//					public void onYesOrNoDialogClick(YesOrNoType yesOrNoType) {
//						// TODO Auto-generated method stub
//						switch (yesOrNoType) {
//						case BtnOk:
//							requestCancelCollect(datas.get(position).getId());
//							break;
//
//						default:
//							break;
//						}
//						
//					}
//				}).show();
//				return false;
//			}
//		});

        requestMsgDatas();
    }

    /**
     * 请求标识
     **/
    private final int REQUEST_MESSAGE_DATAS = 1;
    private final int REQUEST_CANCEL_COLLECT = 3;

    /**
     * 请求数据
     */
    private void requestMsgDatas() {
        showProgressBar(null);
        BeanRequest<LoupanDatas, MamaHaoServerError> req = new BeanRequest<>(
                context,
                "",
                this,
                LoupanDatas.class);

        req.setParam("apiCode", "_user_favorite_lists");
        req.setParam("userId", MyApplication.getInstance().getMemberId());
        req.setParam("lon", MyApplication.getInstance().getLon() + "");//100-120万
        req.setParam("lat", MyApplication.getInstance().getLat() + "");//100-120万

        //我的收藏夹
        // $apiCode='_user_favorite_lists';
        // $param = array();
        // $param['userId'] = '118';
        // $param['lon'] = '120.22';
        // $param['lat'] = '30.19';


        //获取token
        String token = NetworkManager.getInstance().getPostToken(req.getParam());
        req.setParam("token", token);

        addRequestQueue(req, REQUEST_MESSAGE_DATAS, new ReqTag.Builder().handleSimpleRes(true));

    }

    /**
     * 收藏商品
     */
    private void requestCancelCollect(String id) {
        showProgressBar(null);

        JsonElementRequest<DefaultMamahaoServerError> req = new JsonElementRequest<DefaultMamahaoServerError>(
                context, "", this);

        req.setParam("apiCode", "_user_favorite_del");
        req.setParam("userId", MyApplication.getInstance().getMemberId());
        req.setParam("buildingId", id);


        //获取token
        String token = NetworkManager.getInstance().getPostToken(req.getParam());
        req.setParam("token", token);

        addRequestQueue(req, REQUEST_CANCEL_COLLECT, new ReqTag.Builder().handleSimpleRes(true));
    }

    public class LoupanDatas {
        private List<HomeGoodsDatas> lists;

        public List<HomeGoodsDatas> getLists() {
            return lists;
        }

        public void setLists(List<HomeGoodsDatas> lists) {
            this.lists = lists;
        }

    }

    @Override
    protected void onResponseSuccess(ReqTag tag, Object data) {
        // TODO Auto-generated method stub
        super.onResponseSuccess(tag, data);
        switch (tag.getReqId()) {
            case REQUEST_MESSAGE_DATAS:
                datas.clear();
                datas.addAll(((LoupanDatas) data).getLists());
                adapter.notifyDataSetChanged();
                break;
            case REQUEST_CANCEL_COLLECT:
                requestMsgDatas();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResponseFailure(ReqTag tag, MamaHaoServerError error,
                                     MamaHaoError clientError) {
        // TODO Auto-generated method stub
        super.onResponseFailure(tag, error, clientError);
        switch (tag.getReqId()) {
            case REQUEST_MESSAGE_DATAS:
                showBlankPage(null, R.drawable.center_icon_favicon, "暂时没有收藏", null, Constant.DEFAULT_TOP_MARGIN);
                break;

            default:
                break;
        }
    }

    @Override
    protected void processClick(View v) {
        // TODO Auto-generated method stub

    }

}
