package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PayLoadParser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_load_parser);

        ListView listView = findViewById(R.id.payloadItems);

        ArrayList<Item> items = new ArrayList<>();

        try {
            parseJSON(toParse);
        } catch (Exception e){
            System.out.println("JSON Exception");
        }
    }

    String toParse = "{\n" +
            "  \"status\": \"Succeeded\",\n" +
            "  \"recognitionResults\": [{\n" +
            "    \"page\": 1,\n" +
            "    \"clockwiseOrientation\": 358.76,\n" +
            "    \"width\": 338,\n" +
            "    \"height\": 450,\n" +
            "    \"unit\": \"pixel\",\n" +
            "    \"lines\": [{\n" +
            "      \"boundingBox\": [46, 41, 301, 35, 302, 50, 47, 55],\n" +
            "      \"text\": \"*** LITTLE SIX CASINO *$ $\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [50, 43, 75, 42, 76, 54, 51, 55],\n" +
            "        \"text\": \"***\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [93, 41, 145, 39, 145, 53, 94, 54],\n" +
            "        \"text\": \"LITTLE\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [164, 39, 189, 38, 189, 52, 164, 53],\n" +
            "        \"text\": \"SIX\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [208, 38, 259, 38, 259, 51, 208, 52],\n" +
            "        \"text\": \"CASINO\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [278, 38, 293, 38, 293, 49, 278, 50],\n" +
            "        \"text\": \"*$\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [295, 38, 301, 38, 301, 49, 295, 49],\n" +
            "        \"text\": \"$\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [39, 72, 136, 68, 137, 83, 40, 86],\n" +
            "      \"text\": \"23181 MIKKI\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [41, 72, 87, 72, 87, 85, 41, 86],\n" +
            "        \"text\": \"23181\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [93, 71, 136, 70, 136, 83, 93, 85],\n" +
            "        \"text\": \"MIKKI\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [40, 103, 129, 100, 130, 114, 41, 117],\n" +
            "      \"text\": \"1403/1\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [44, 104, 129, 101, 129, 115, 44, 117],\n" +
            "        \"text\": \"1403/1\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [189, 99, 266, 98, 267, 112, 190, 114],\n" +
            "      \"text\": \"CHK 5383\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [191, 100, 218, 100, 218, 114, 191, 114],\n" +
            "        \"text\": \"CHK\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [228, 99, 264, 99, 264, 113, 228, 113],\n" +
            "        \"text\": \"5383\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [270, 98, 324, 97, 324, 111, 271, 112],\n" +
            "      \"text\": \"GST 2\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [280, 98, 308, 98, 308, 112, 280, 112],\n" +
            "        \"text\": \"GST\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [317, 98, 324, 97, 324, 111, 317, 111],\n" +
            "        \"text\": \"2\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [111, 116, 253, 114, 254, 130, 112, 131],\n" +
            "      \"text\": \"JUN10 '16 11:30AM\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [112, 117, 155, 116, 155, 131, 113, 131],\n" +
            "        \"text\": \"JUN10\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [158, 116, 185, 116, 184, 131, 158, 131],\n" +
            "        \"text\": \"'16\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [194, 116, 254, 116, 253, 130, 194, 131],\n" +
            "        \"text\": \"11:30AM\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [110, 148, 234, 148, 234, 163, 111, 164],\n" +
            "      \"text\": \"Dine In\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [112, 149, 177, 149, 178, 164, 112, 165],\n" +
            "        \"text\": \"Dine\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [205, 149, 230, 148, 230, 164, 205, 164],\n" +
            "        \"text\": \"In\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [109, 182, 254, 181, 254, 194, 110, 196],\n" +
            "      \"text\": \"* * * * * S# 1 * ***#\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [113, 183, 119, 183, 119, 195, 113, 195],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [122, 183, 129, 183, 129, 195, 121, 195],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [131, 183, 138, 183, 138, 195, 131, 195],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [141, 183, 147, 183, 147, 196, 141, 195],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [149, 183, 157, 183, 157, 196, 149, 196],\n" +
            "        \"text\": \"*\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [168, 182, 184, 182, 184, 196, 168, 196],\n" +
            "        \"text\": \"S#\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [195, 182, 203, 182, 203, 195, 196, 195],\n" +
            "        \"text\": \"1\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [212, 182, 219, 182, 219, 195, 212, 195],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [221, 182, 253, 181, 254, 193, 221, 195],\n" +
            "        \"text\": \"***#\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [55, 199, 228, 196, 229, 212, 56, 215],\n" +
            "      \"text\": \"2 DIET PEPSI @ 1.99\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [57, 201, 67, 201, 68, 215, 59, 215],\n" +
            "        \"text\": \"2\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [74, 200, 113, 199, 114, 214, 76, 215],\n" +
            "        \"text\": \"DIET\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [121, 199, 169, 198, 170, 213, 122, 214],\n" +
            "        \"text\": \"PEPSI\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [177, 198, 186, 198, 186, 213, 177, 213],\n" +
            "        \"text\": \"@\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [196, 198, 229, 198, 229, 212, 197, 213],\n" +
            "        \"text\": \"1.99\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [263, 197, 299, 196, 299, 210, 263, 210],\n" +
            "      \"text\": \"3. 98\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [266, 195, 281, 195, 281, 209, 266, 209],\n" +
            "        \"text\": \"3.\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [284, 195, 298, 195, 298, 209, 284, 209],\n" +
            "        \"text\": \"98\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [56, 216, 211, 213, 212, 228, 57, 232],\n" +
            "      \"text\": \"1 CASINO SANDWICH\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [59, 218, 68, 218, 69, 232, 60, 232],\n" +
            "        \"text\": \"1\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [76, 217, 131, 216, 131, 230, 76, 232],\n" +
            "        \"text\": \"CASINO\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [141, 215, 211, 215, 211, 229, 141, 230],\n" +
            "        \"text\": \"SANDWICH\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [263, 213, 301, 212, 300, 226, 263, 227],\n" +
            "      \"text\": \"5. 99\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [266, 212, 281, 212, 281, 226, 266, 226],\n" +
            "        \"text\": \"5.\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [284, 212, 299, 212, 300, 225, 284, 226],\n" +
            "        \"text\": \"99\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [54, 233, 185, 231, 186, 246, 55, 249],\n" +
            "      \"text\": \"1 WALLEYE SAND\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [60, 235, 68, 234, 68, 248, 60, 248],\n" +
            "        \"text\": \"1\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [76, 234, 141, 232, 141, 247, 76, 248],\n" +
            "        \"text\": \"WALLEYE\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [150, 232, 186, 232, 186, 246, 150, 247],\n" +
            "        \"text\": \"SAND\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [254, 230, 302, 228, 303, 243, 255, 244],\n" +
            "      \"text\": \"11. 99\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [259, 230, 283, 229, 282, 244, 259, 244],\n" +
            "        \"text\": \"11.\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [285, 229, 303, 229, 302, 243, 285, 244],\n" +
            "        \"text\": \"99\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [37, 252, 67, 251, 66, 266, 37, 265],\n" +
            "      \"text\": \"TAX\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [40, 250, 66, 250, 66, 265, 40, 265],\n" +
            "        \"text\": \"TAX\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [119, 249, 302, 244, 303, 260, 120, 264],\n" +
            "      \"text\": \"1. 62 TOTAL DU 23 . 58\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [124, 250, 138, 249, 138, 264, 124, 265],\n" +
            "        \"text\": \"1.\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [141, 249, 160, 249, 160, 264, 141, 264],\n" +
            "        \"text\": \"62\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [178, 248, 223, 247, 223, 262, 179, 263],\n" +
            "        \"text\": \"TOTAL\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [231, 247, 250, 246, 250, 262, 231, 262],\n" +
            "        \"text\": \"DU\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [259, 246, 275, 246, 275, 261, 259, 261],\n" +
            "        \"text\": \"23\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [278, 246, 283, 246, 283, 261, 278, 261],\n" +
            "        \"text\": \".\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [286, 246, 302, 246, 303, 261, 286, 261],\n" +
            "        \"text\": \"58\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [110, 266, 224, 264, 224, 279, 111, 281],\n" +
            "      \"text\": \"** ALL SEATS\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [115, 267, 132, 267, 132, 280, 115, 280],\n" +
            "        \"text\": \"**\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [142, 266, 168, 266, 168, 280, 142, 280],\n" +
            "        \"text\": \"ALL\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [179, 265, 223, 264, 223, 279, 179, 280],\n" +
            "        \"text\": \"SEATS\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [236, 264, 259, 264, 259, 276, 237, 277],\n" +
            "      \"text\": \"* *\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [242, 263, 249, 263, 249, 276, 242, 276],\n" +
            "        \"text\": \"*\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [252, 263, 258, 263, 258, 276, 252, 276],\n" +
            "        \"text\": \"*\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [75, 303, 113, 302, 113, 316, 75, 317],\n" +
            "      \"text\": \"Food\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [76, 302, 111, 302, 112, 316, 76, 316],\n" +
            "        \"text\": \"Food\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [262, 297, 308, 296, 308, 311, 263, 312],\n" +
            "      \"text\": \"17.98\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [263, 296, 307, 295, 307, 310, 263, 311],\n" +
            "        \"text\": \"17.98\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [72, 320, 160, 319, 161, 334, 73, 336],\n" +
            "      \"text\": \"Beverages\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [75, 321, 160, 320, 161, 334, 75, 336],\n" +
            "        \"text\": \"Beverages\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [269, 315, 312, 313, 312, 328, 270, 330],\n" +
            "      \"text\": \"3.98\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [273, 314, 311, 312, 311, 327, 273, 329],\n" +
            "        \"text\": \"3.98\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [75, 339, 105, 339, 105, 354, 75, 354],\n" +
            "      \"text\": \"TAX\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [77, 339, 105, 339, 105, 354, 77, 354],\n" +
            "        \"text\": \"TAX\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [271, 333, 311, 331, 312, 346, 271, 348],\n" +
            "      \"text\": \"1 . 62\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [275, 331, 282, 331, 283, 347, 276, 347],\n" +
            "        \"text\": \"1\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [286, 331, 290, 331, 291, 347, 286, 347],\n" +
            "        \"text\": \".\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [293, 330, 310, 330, 311, 346, 294, 346],\n" +
            "        \"text\": \"62\"\n" +
            "      }]\n" +
            "    }, {\n" +
            "      \"boundingBox\": [34, 357, 308, 348, 309, 366, 35, 375],\n" +
            "      \"text\": \"12:01 TOTAL DUE $23 . 58\",\n" +
            "      \"words\": [{\n" +
            "        \"boundingBox\": [36, 359, 86, 357, 87, 374, 38, 375],\n" +
            "        \"text\": \"12:01\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [95, 357, 143, 355, 144, 372, 96, 373],\n" +
            "        \"text\": \"TOTAL\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [152, 354, 182, 353, 183, 371, 153, 372],\n" +
            "        \"text\": \"DUE\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [202, 352, 250, 351, 251, 368, 203, 370],\n" +
            "        \"text\": \"$23\",\n" +
            "        \"confidence\": \"Low\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [262, 350, 272, 350, 273, 367, 262, 368],\n" +
            "        \"text\": \".\"\n" +
            "      }, {\n" +
            "        \"boundingBox\": [277, 350, 306, 349, 307, 366, 278, 367],\n" +
            "        \"text\": \"58\"\n" +
            "      }]\n" +
            "    }]\n" +
            "  }]\n" +
            "}";

    private void parseJSON(String s) throws JSONException {
        JSONObject root = new JSONObject(s);
        JSONArray recogResults = root.getJSONArray("recognitionResults");
        for(int page = 0; page < recogResults.length(); page++){
            JSONObject pageLines = recogResults.getJSONObject(page);
            JSONArray lines = pageLines.getJSONArray("lines");
            for(int line = 0; line < lines.length(); line++){
                JSONObject lineData = lines.getJSONObject(line);
                System.out.println(lineData.get("text"));
            }
        }
    }
}
