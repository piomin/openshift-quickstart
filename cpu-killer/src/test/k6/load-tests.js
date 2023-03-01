import {
  Writer,
  SchemaRegistry,
  SCHEMA_TYPE_JSON,
} from "k6/x/kafka";

const writer = new Writer({
  brokers: ["localhost:55001"],
  topic: "events",
});

const schemaRegistry = new SchemaRegistry();

export default function () {
  writer.produce({
    messages: [
      {
        value: schemaRegistry.serialize({
          data: {
            id: 1
          },
          schemaType: SCHEMA_TYPE_JSON,
        }),
      },
    ],
  });
}

export function teardown(data) {
  writer.close();
}