/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
package com.github.hadilq.cleanarchitecturefp.data.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

class FileReader {

    @Throws(Exception::class)
    fun readString(file: String): String {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(file)
        val writer = StringWriter()

        val buffer = CharArray(1024)
        inputStream.use { stream ->
            val reader = BufferedReader(
                InputStreamReader(stream, "UTF-8")
            )
            var n: Int
            while (run { n = reader.read(buffer); n } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()
    }
}
