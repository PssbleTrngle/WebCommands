<template>
  <div ref="chat" id="chat">
    <p v-for="(msg, i) in messages" :key="i">
      <span>{{ format(msg.timestamp) }}</span>
      <span>{{ msg.text }}</span>
    </p>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import _ from "lodash";
import moment from "moment";
import { EventHub } from "../main";

export interface Message {
  text: string;
  timestamp: number;
  sender?: string;
}

@Component({
  created: function() {
    EventHub.$on("scroll", () =>
      setTimeout(() => {
        const chat = this.$refs.chat as Element | null;
        chat?.scrollTo(0, chat.scrollHeight);
      }, 100)
    );

    EventHub.$emit("scroll");
  }
})
export default class Chat extends Vue {
  @Prop({ required: true })
  messages!: Message[];

  format(date: number) {
    return `[${moment(date).format("HH:mm:ss")}]`;
  }
}
</script>

<style lang='scss' scoped>
#chat {
  padding: 5px 20px;
  padding-bottom: 20px;
  overflow-y: scroll;
  overflow-x: hidden;

  &::-webkit-scrollbar {
    width: 10px;
  }

  &::-webkit-scrollbar-thumb {
    background: #0002;
    border-radius: 1000px;
  }

  &::-webkit-scrollbar-track {
    background: #0002;
  }

  p {
    padding: 2px 0;
    transition: background 0.1s linear;

    span:first-child {
      margin-right: 10px;
      opacity: 0.5;
    }

    &:hover {
      background-color: #fff1;
    }
  }
}
</style>
