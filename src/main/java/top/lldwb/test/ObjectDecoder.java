package top.lldwb.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 接收对象
 *
 * @author lldwb
 * @email 3247187440@qq.com
 * @date 2023/12/6
 * @time 8:32
 * @PROJECT_NAME SpringBootTest
 */
public class ObjectDecoder extends ByteToMessageDecoder {
    public ObjectDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    private Class<?> clazz;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws IOException {
        // 判断输入流中是否有数据
        if (in.readableBytes() <= 0) {
            return;
        }

        // 创建一个字节数组，用于存放输入流中的数据
        byte[] bytes = new byte[in.readableBytes()];
        // 读取输入流中的数据到字节数组中
        in.readBytes(bytes);

        // 使用ObjectMapper将字节数组转换成指定类型的对象，并添加到输出流中
        out.add(new ObjectMapper().readValue(bytes, clazz));
    }
}