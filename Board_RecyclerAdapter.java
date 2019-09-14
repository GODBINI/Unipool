package com.unipool.unipool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Board_RecyclerAdapter extends RecyclerView.Adapter<Board_RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<board_data> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Board_RecyclerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(board_data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    void init() {
        listData.clear();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView board_schoolText;
        private TextView board_titleText;
        private TextView board_commentText;
        private TextView board_leaderuserIDText;
        private TextView board_trustText;
        private TextView board_quantityText;
        private Button board_requestButton;
        private String user;
        private String leader_user;

        ItemViewHolder(final View itemView) {
            super(itemView);
            board_schoolText = itemView.findViewById(R.id.board_schoolText);
            board_titleText = itemView.findViewById(R.id.board_titleText);
            board_commentText = itemView.findViewById(R.id.board_commentText);
            board_leaderuserIDText = itemView.findViewById(R.id.board_userIDText);
            board_trustText = itemView.findViewById(R.id.board_trustText);
            board_quantityText = itemView.findViewById(R.id.board_quantityText);
            board_requestButton = itemView.findViewById(R.id.board_requestButton);
        }

        void onBind(board_data data) {
            board_schoolText.setText(data.getBoard_schoolText());
            board_titleText.setText(data.getBoard_titleText());
            board_commentText.setText(data.getBoard_commentText());
            board_leaderuserIDText.setText(data.getBoard_userIDText() + "님");
            leader_user = data.getBoard_userIDText();
            board_trustText.setText(data.getBoard_trustText() + "점");
            user = data.getUser();
            final String quantity = data.getBoard_quantityText();
            if(quantity.equals("1"))
                board_quantityText.setText("1/4");
            else if(quantity.equals("2"))
                board_quantityText.setText("2/4");
            else if(quantity.equals("3"))
                board_quantityText.setText("3/4");
            else if(quantity.equals("4"))
                board_quantityText.setText("4/4");

            board_requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (user.equals(leader_user)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("자신의 모집글에는 신청하실수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                    else if(quantity.equals("4")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("정원 초과입니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                    else {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(view.getContext(), "신청완료", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(view.getContext(), "이미 모집글에 속해있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ApplyRequest applyRequest = new ApplyRequest(user, leader_user, responseListener);
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        requestQueue.add(applyRequest);
                    }
                }
            });
        }
    }
}
