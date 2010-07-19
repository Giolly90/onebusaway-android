/*
 * Copyright (C) 2010 Paul Watts (paulcwatts@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joulespersecond.oba.request;

import com.joulespersecond.oba.ObaApi;
import com.joulespersecond.oba.ObaHelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.Reader;

/**
 * The base class for Oba requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public class RequestBase {
    protected final Uri mUri;

    protected RequestBase(Uri uri) {
        mUri = uri;
    }

    private static String getServer(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("preferences_oba_api_servername",
                "api.onebusaway.org");
    }

    public static class BuilderBase {
        private static final String API_KEY = "v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc=cGF1bGN3YXR0c0BnbWFpbC5jb20=";
        protected static final String BASE_PATH = "/api/where";

        protected final Uri.Builder mBuilder;

        protected BuilderBase(Context context, String path) {
            mBuilder = new Uri.Builder();
            mBuilder.scheme("http");
            mBuilder.authority(getServer(context));
            mBuilder.path(path);
            mBuilder.appendQueryParameter("version", "2");
            mBuilder.appendQueryParameter("key", API_KEY);
            // TODO: App/User analytics info.
        }

        protected static String getPathWithId(String pathElement, String id) {
            StringBuilder builder = new StringBuilder(BASE_PATH);
            builder.append(pathElement);
            builder.append(id);
            builder.append(".json");
            return builder.toString();
        }

        protected Uri buildUri() {
            return mBuilder.build();
        }
    }

    protected <T> T call(Class<T> cls) throws IOException {
        final Reader reader = ObaHelp.getUri(mUri);
        return ObaApi.getGson().fromJson(reader, cls);
    }
}