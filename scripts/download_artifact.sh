GITHUB_TOKEN=$1

VERSION=$(curl -L \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        https://api.github.com/user/packages/maven/whatsappbroski.whatsappbroski/versions | \
        jq -r '.[] | select(.name | test("SNAPSHOT") | not) | .name' | sort -V | tail -n1)

curl -L \
  -H "Authorization: Bearer ${GITHUB_TOKEN}" \
  -o app.jar \
  "https://maven.pkg.github.com/whatsappbroski/whatsappbroski/whatsappbroski/whatsappbroski/$VERSION/whatsappbroski-$VERSION.jar"